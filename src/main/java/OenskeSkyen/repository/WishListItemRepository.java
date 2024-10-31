package OenskeSkyen.repository;

import OenskeSkyen.model.WishListItem;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class WishListItemRepository {
    private final JdbcTemplate jdbcTemplate;

    public WishListItemRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public WishListItem save(WishListItem item) {
        String sql = "INSERT INTO wish_list_items (item_name, description, user_id) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, item.getItemName(), item.getDescription(), item.getUserId());

        // Optionally, you can retrieve the inserted item or its ID if needed
        return item; // Return the item or adjust as needed
    }

    // Method to find all WishListItems by user ID
    public List<WishListItem> findByUserId(Long userId) {
        String sql = "SELECT * FROM wish_list_items WHERE user_id = ?";
        return jdbcTemplate.query(sql, new Object[]{userId}, new WishListItemRowMapper());
    }

    // Inner class to map ResultSet to WishListItem
    private static class WishListItemRowMapper implements RowMapper<WishListItem> {
        @Override
        public WishListItem mapRow(ResultSet rs, int rowNum) throws SQLException {
            WishListItem item = new WishListItem();
            item.setId(rs.getLong("id")); // Make sure the WishListItem class has an 'id' field
            item.setItemName(rs.getString("item_name"));
            item.setDescription(rs.getString("description"));
            // Assuming your WishListItem has a User field; you'll need to set that if required
            // item.setUser(user); // You may need to retrieve the user based on user ID
            return item;
        }
    }
}