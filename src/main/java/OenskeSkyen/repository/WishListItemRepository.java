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
    // JdbcTemplate bruges til at interagere med databasen
    private final JdbcTemplate jdbcTemplate;

    // Konstruktor, der initialiserer JdbcTemplate med den givne DataSource
    public WishListItemRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    // Finder alle ønskeliste-elementer, der tilhører en bestemt bruger
    public List<WishListItem> findByUserId(Long userId) {
        String sql = "SELECT * FROM wishlist_items WHERE user_id = ?";
        // Udfører en forespørgsel og mapper resultatet til en liste af WishListItem-objekter
        return jdbcTemplate.query(sql, new Object[]{userId}, new WishListItemRowMapper());
    }

    // Privat statisk klasse til at mappe rækker fra databasen til WishListItem-objekter
    private static class WishListItemRowMapper implements RowMapper<WishListItem> {
        @Override
        public WishListItem mapRow(ResultSet rs, int rowNum) throws SQLException {
            // Opretter et nyt WishListItem-objekt og udfylder det med data fra resultatsættet
            WishListItem item = new WishListItem();
            item.setId(rs.getLong("id")); // Sætter ID
            item.setItemName(rs.getString("item_name")); // Sætter navn på element
            item.setDescription(rs.getString("item_description")); // Sætter beskrivelse
            item.setUserId(rs.getLong("user_id")); // Sætter brugerens ID
            item.setPrice(rs.getDouble("price")); // Sætter pris
            item.setIsReserved(rs.getInt("is_reserved")); // Sætter reserveringsstatus
            return item; // Returnerer det udfyldte objekt
        }
    }

    // Henter alle kategorier fra databasen
    public List<Category> findAllCategories() {
        String sql = "SELECT * FROM item_categories";
        // Udfører en forespørgsel og mapper resultatet til en liste af Category-objekter
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            return new Category(rs.getLong("id"), rs.getString("category_name"));
        });
    }

    // Finder alle ønskeliste-elementer i databasen
    public List<WishListItem> findAll() {
        String sql = "SELECT * FROM wishlist_items";
        // Udfører en forespørgsel og bruger WishListItemRowMapper til at mappe resultatet
        return jdbcTemplate.query(sql, new WishListItemRowMapper());
    }

    // Reserverer et ønskeliste-element ved at sætte is_reserved til 1
    public boolean reserveItem(Long itemId) {
        String sql = "UPDATE wishlist_items SET is_reserved = 1 WHERE id = ?";
        // Udfører en opdatering og returnerer true, hvis mindst én række blev påvirket
        int rowsAffected = jdbcTemplate.update(sql, itemId);
        System.out.println("Rows affected: " + rowsAffected);
        return rowsAffected > 0;
    }

    // Fjerner reservationen af et ønskeliste-element ved at sætte is_reserved til 0
    public boolean unreserveItem(Long itemId) {
        String sql = "UPDATE wishlist_items SET is_reserved = 0 WHERE id = ?";
        // Udfører en opdatering og returnerer true, hvis mindst én række blev påvirket
        int rowsAffected = jdbcTemplate.update(sql, itemId);
        return rowsAffected > 0;
    }

    // Finder et specifikt ønskeliste-element baseret på dets ID
    public WishListItem findById(Long itemId) {
        String sql = "SELECT * FROM wishlist_items WHERE id = ?";
        // Udfører en forespørgsel og mapper resultatet til et WishListItem-objekt
        return jdbcTemplate.queryForObject(sql, new Object[]{itemId}, new WishListItemRowMapper());
    }
}
