package OenskeSkyen.repository;

import OenskeSkyen.model.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

// UserRowMapper bruges til at mappe rækker fra en SQL-forespørgsel til et User-objekt
public class UserRowMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        // Opretter et nyt User-objekt
        User user = new User();
        // Sætter brugerens ID baseret på kolonnen "id" fra resultatet
        user.setId(rs.getLong("id"));
        // Sætter brugernavn baseret på kolonnen "username" fra resultatet
        user.setUsername(rs.getString("username"));
        // Sætter adgangskode baseret på kolonnen "password" fra resultatet
        user.setPassword(rs.getString("password"));
        // Sætter enabled-status baseret på kolonnen "enabled" fra resultatet
        user.setEnabled(rs.getBoolean("enabled"));
        // Returnerer det udfyldte User-objekt
        return user;
    }
}
