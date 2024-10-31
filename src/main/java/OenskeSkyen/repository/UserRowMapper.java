package OenskeSkyen.repository;

import OenskeSkyen.model.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("id")); // Adjust according to your User model's field names
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setEnabled(rs.getBoolean("enabled"));
        return user;
    }
}