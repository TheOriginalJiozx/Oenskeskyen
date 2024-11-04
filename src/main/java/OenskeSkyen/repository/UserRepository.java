package OenskeSkyen.repository;

import OenskeSkyen.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class UserRepository {
    private final JdbcTemplate jdbcTemplate;

    public UserRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public User findByUsername(String username) {
        String sql = "SELECT * FROM wish_users WHERE username = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{username}, new UserRowMapper());
    }

    public Long getUserIdByUsername(String username) {
        return jdbcTemplate.queryForObject(
                "SELECT id FROM wish_users WHERE username = ?",
                new Object[]{username},
                Long.class
        );
    }
}