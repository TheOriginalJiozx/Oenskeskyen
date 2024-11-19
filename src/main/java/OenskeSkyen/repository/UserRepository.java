package OenskeSkyen.repository;

import OenskeSkyen.model.User;
import OenskeSkyen.model.WishListItem;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class UserRepository {
    // JdbcTemplate bruges til at udføre SQL-forespørgsler mod databasen
    private final JdbcTemplate jdbcTemplate;

    // Konstruktor, der initialiserer JdbcTemplate med den givne DataSource
    public UserRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    // Metode til at finde en bruger baseret på brugernavn
    public User findByUsername(String username) {
        String sql = "SELECT * FROM wish_users WHERE username = ?";
        // Udfører en forespørgsel og mapper resultatet til en User-objekt
        return jdbcTemplate.queryForObject(sql, new Object[]{username}, new UserRowMapper());
    }

    // Metode til at hente en brugers ID baseret på deres brugernavn
    public Long getUserIdByUsername(String username) {
        return jdbcTemplate.queryForObject(
                "SELECT id FROM wish_users WHERE username = ?",
                new Object[]{username},
                Long.class // Returnerer ID'et som en Long
        );
    }

    // Metode til at hente ønskeliste-elementer for en given bruger baseret på brugernavn
    public Object getWishListItemsForUser(String username) {
        return jdbcTemplate.query(
                "SELECT * FROM wishlist_items WHERE user_id = (SELECT id FROM wish_users WHERE username = ?)",
                new Object[]{username},
                // Mapper hver række i resultatet til et WishListItem-objekt
                (rs, rowNum) -> {
                    WishListItem item = new WishListItem();
                    item.setId(rs.getLong("id")); // Sætter ID
                    item.setItemName(rs.getString("item_name")); // Sætter navn på ønskeliste-element
                    item.setDescription(rs.getString("item_description")); // Sætter beskrivelse
                    item.setPrice(rs.getDouble("price")); // Sætter pris
                    item.setIsReserved(rs.getInt("is_reserved")); // Sætter reserveringsstatus
                    return item; // Returnerer det udfyldte WishListItem-objekt
                }
        );
    }
}
