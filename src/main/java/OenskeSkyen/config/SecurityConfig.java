package OenskeSkyen.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) // Aktiver sikkerhedsanmærkninger til metode-niveau
public class SecurityConfig {

    // Indlæser databaseoplysninger fra konfigurationsfiler
    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String dbUsername;

    @Value("${spring.datasource.password}")
    private String dbPassword;

    // Konfiguration af H2-datakilde kun for "h2"-profilen
    @Bean
    @Profile("h2")
    public DataSource h2DataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUrl(dbUrl);
        dataSource.setUsername(dbUsername);
        dataSource.setPassword(dbPassword);
        return dataSource;
    }

    // Bean til JdbcTemplate for databaseinteraktioner
    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    // Konfigurerer sikkerhedsfilterkæden
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/", "/header.html", "/footer.html", "/h2-console/**", "/css/**", "/wishlist/reserve").permitAll() // Offentlige endpoints
                        .requestMatchers("/login", "/signup").anonymous() // Tilgængelig kun for uautentificerede brugere
                        .anyRequest().authenticated() // Alle andre endpoints kræver autentifikation
                )
                .formLogin(form -> form
                        .loginPage("/login") // Brugerdefineret login-side
                        .defaultSuccessUrl("/", true) // Standard-side efter succesfuld login
                )
                .logout(logout -> logout
                        .logoutUrl("/logout") // URL til logout
                        .logoutSuccessUrl("/login?logout") // Side der vises efter logout
                )
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/h2-console/**", "/wishlist/add", "/wishlist/reserve") // Deaktiver CSRF for specifikke endpoints
                )
                .headers(headers -> headers
                        .frameOptions().sameOrigin() // Tillad iframes fra samme oprindelse (krævet for H2 Console)
                        .httpStrictTransportSecurity(hsts -> hsts.disable()) // Deaktiver HSTS
                );

        return http.build();
    }

    // Konfigurerer autentifikation med brugeroplysninger fra databasen
    @Bean
    public DaoAuthenticationProvider authenticationProvider(JdbcTemplate jdbcTemplate) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(username -> {
            String sql = "SELECT username, password, enabled FROM wish_users WHERE username = ?"; // SQL-forespørgsel for at hente brugeroplysninger
            return jdbcTemplate.queryForObject(sql, new Object[]{username}, (rs, rowNum) -> {
                String user = rs.getString("username");
                String password = rs.getString("password");
                boolean enabled = rs.getBoolean("enabled");
                return org.springframework.security.core.userdetails.User.withUsername(user)
                        .password(password)
                        .accountLocked(!enabled) // Lås kontoen hvis den er deaktiveret
                        .authorities("USER") // Standard rolle
                        .build();
            });
        });
        authProvider.setPasswordEncoder(passwordEncoder()); // Sætter password encoder
        return authProvider;
    }

    // Definerer en password encoder ved brug af BCrypt
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
