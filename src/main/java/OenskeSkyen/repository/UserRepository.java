package OenskeSkyen.repository;

import OenskeSkyen.model.User;
import OenskeSkyen.model.WishListItem;
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

    public Object getWishListItemsForUser(String username) {
        return jdbcTemplate.query(
                "SELECT * FROM wishlist_items WHERE user_id = (SELECT id FROM wish_users WHERE username = ?)",
                new Object[]{username},
                (rs, rowNum) -> {
                    WishListItem item = new WishListItem();
                    item.setId(rs.getLong("id"));
                    item.setItemName(rs.getString("item_name"));
                    item.setDescription(rs.getString("item_description"));
                    item.setPrice(rs.getDouble("price"));
                    item.setIsReserved(rs.getInt("is_reserved"));
                    return item;
                }
        );
    }
}