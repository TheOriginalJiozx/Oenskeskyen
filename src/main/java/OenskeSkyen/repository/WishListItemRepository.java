package OenskeSkyen.repository;

import OenskeSkyen.model.Category;
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

    public List<WishListItem> findByUserId(Long userId) {
        String sql = "SELECT * FROM wishlist_items WHERE user_id = ?";
        return jdbcTemplate.query(sql, new Object[]{userId}, new WishListItemRowMapper());
    }

    private static class WishListItemRowMapper implements RowMapper<WishListItem> {
        @Override
        public WishListItem mapRow(ResultSet rs, int rowNum) throws SQLException {
            WishListItem item = new WishListItem();
            item.setId(rs.getLong("id"));
            item.setItemName(rs.getString("item_name"));
            item.setDescription(rs.getString("item_description"));
            item.setUserId(rs.getLong("user_id"));
            item.setPrice(rs.getDouble("price"));
            item.setIsReserved(rs.getInt("is_reserved"));
            return item;
        }
    }

    public List<Category> findAllCategories() {
        String sql = "SELECT * FROM item_categories";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            return new Category(rs.getLong("id"), rs.getString("category_name"));
        });
    }

    public List<WishListItem> findAll() {
        String sql = "SELECT * FROM wishlist_items";
        return jdbcTemplate.query(sql, new WishListItemRowMapper());
    }

    public boolean reserveItem(Long itemId) {
        String sql = "UPDATE wishlist_items SET is_reserved = 1 WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, itemId);
        System.out.println("Rows affected: " + rowsAffected);
        return rowsAffected > 0;
    }

    public boolean unreserveItem(Long itemId) {
        String sql = "UPDATE wishlist_items SET is_reserved = 0 WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, itemId);
        return rowsAffected > 0;
    }

    public WishListItem findById(Long itemId) {
        String sql = "SELECT * FROM wishlist_items WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{itemId}, new WishListItemRowMapper());
    }
}