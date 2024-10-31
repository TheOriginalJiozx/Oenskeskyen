package OenskeSkyen.repository;

import OenskeSkyen.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository {
    private final JdbcTemplate jdbcTemplate;

    public UserRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    // Method to find a user by username
    public User findByUsername(String username) {
        String sql = "SELECT * FROM wish_users WHERE username = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{username}, new UserRowMapper());
    }

    // Method to find a user by ID
    public Optional<User> findById(Long id) {
        String sql = "SELECT * FROM wish_users WHERE id = ?";
        try {
            User user = jdbcTemplate.queryForObject(sql, new Object[]{id}, new UserRowMapper());
            return Optional.of(user);
        } catch (Exception e) {
            return Optional.empty(); // Return empty if not found
        }
    }

    // Optional: Method to get all users
    public List<User> findAll() {
        String sql = "SELECT * FROM wish_users";
        return jdbcTemplate.query(sql, new UserRowMapper());
    }
}