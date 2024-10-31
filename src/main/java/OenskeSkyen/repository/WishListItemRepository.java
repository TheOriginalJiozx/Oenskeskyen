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
        String sql = "INSERT INTO wishlist_items (item_name, item_description, user_id) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, item.getItemName(), item.getDescription(), item.getUserId());

        return item;
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

            return item;
        }
    }
}