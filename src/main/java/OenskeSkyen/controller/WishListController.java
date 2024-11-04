package OenskeSkyen.controller;

import OenskeSkyen.model.Category;
import OenskeSkyen.model.User;
import OenskeSkyen.model.WishItem;
import OenskeSkyen.model.WishListItem;
import OenskeSkyen.repository.UserRepository;
import OenskeSkyen.repository.WishListItemRepository;
import OenskeSkyen.service.WishListService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/")
public class WishListController {
    private final WishListService wishListService;
    private final JdbcTemplate jdbcTemplate;
    private final PasswordEncoder passwordEncoder;
    private final WishListItemRepository wishListItemRepository;
    private final UserRepository userRepository;

    public WishListController(WishListService wishListService, JdbcTemplate jdbcTemplate, PasswordEncoder passwordEncoder, WishListItemRepository wishListItemRepository, UserRepository userRepository) {
        this.wishListService = wishListService;
        this.jdbcTemplate = jdbcTemplate;
        this.passwordEncoder = passwordEncoder;
        this.wishListItemRepository = wishListItemRepository;
        this.userRepository = userRepository;
    }

    private String getAuthenticatedUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName()))
                ? auth.getName() : "Guest";
    }

    @GetMapping("/")
    public String showIndexPage(Model model) {
        model.addAttribute("username", getAuthenticatedUsername());
        return "index";
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("username", getAuthenticatedUsername());
        return "login";
    }

    @PostMapping("/login")
    public String doLogin(@RequestParam String username, @RequestParam String password) {
        System.out.println("Attempting login for user: " + username);
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout() {
        return "logout";
    }

    @PostMapping("/logout")
    public String doLogout(@RequestParam String username, @RequestParam String password) {
        System.out.println("Attempting logout for user: " + username);
        return "redirect:/";
    }

    @GetMapping("/home")
    public String home(Model model) {
        List<WishListItem> wishListItems = wishListService.getWishListItems();
        model.addAttribute("wishListItems", wishListItems);
        return "home";
    }

    @GetMapping("/wishlist/view")
    public String viewWishList(@RequestParam(required = false) String user, Model model) {
        String authenticatedUsername = getAuthenticatedUsername();

        if (user == null || user.isEmpty()) {
            return "redirect:/wishlist/view?user=" + authenticatedUsername;
        }

        model.addAttribute("username", authenticatedUsername);
        model.addAttribute("wishlistOwner", user);

        Long userId = userRepository.getUserIdByUsername(user);
        if (userId == null) {

            return "redirect:/wishlist/view?user=" + authenticatedUsername;
        }

        model.addAttribute("wishlistUserId", userId);

        List<WishListItem> wishListItems = wishListService.getWishListItemsForUser(user);
        model.addAttribute("wishListItems", wishListItems);

        return "view";
    }

    @GetMapping("wishlist/add")
    public String addWishListItemForm(@RequestParam(required = false) String selectedCategory, Model model) {
        String username = getAuthenticatedUsername();
        model.addAttribute("username", username);

        Integer userId = jdbcTemplate.queryForObject("SELECT id FROM wish_users WHERE username = ?", new Object[]{username}, Integer.class);
        model.addAttribute("categories", wishListService.getAllCategories());

        List<WishItem> wishItems = List.of();

        if (selectedCategory != null && !selectedCategory.isEmpty()) {
            wishItems = jdbcTemplate.query("SELECT * FROM wish_items WHERE category = (SELECT category_name FROM item_categories WHERE category_name = ?)",
                    new Object[]{selectedCategory}, (rs, rowNum) -> {
                        WishItem item = new WishItem();
                        item.setId(rs.getLong("id"));
                        item.setItemName(rs.getString("item_name"));
                        item.setDescription(rs.getString("item_description"));
                        item.setPrice(rs.getDouble("price"));
                        return item;
                    });
        }

        model.addAttribute("wishItems", wishItems);
        return "add";
    }

    @GetMapping("/wishlist/items")
    @ResponseBody
    public List<WishItem> getItemsByCategory(@RequestParam(required = false) String category) {
        String sql = "SELECT * FROM wish_items";
        List<Object> params = new ArrayList<>();

        if (category != null && !category.isEmpty()) {
            sql += " WHERE category = ?";
            params.add(category);
        }

        return jdbcTemplate.query(sql, params.toArray(), (rs, rowNum) -> {
            WishItem item = new WishItem();
            item.setId(rs.getLong("id"));
            item.setItemName(rs.getString("item_name"));
            item.setDescription(rs.getString("item_description"));
            item.setPrice(rs.getDouble("price"));
            return item;
        });
    }

    @GetMapping("/wishlist/items/reserve")
    @ResponseBody
    public List<WishListItem> getReserveItems(@RequestParam Long userId) {
        return jdbcTemplate.query(
                "SELECT * FROM wishlist_items WHERE user_id = ?",
                new Object[]{userId},
                (rs, rowNum) -> {
                    WishListItem item = new WishListItem();
                    item.setId(rs.getLong("id"));
                    item.setItemName(rs.getString("item_name"));
                    item.setDescription(rs.getString("description"));
                    item.setPrice(rs.getDouble("price"));
                    item.setIsReserved(rs.getInt("is_reserved"));
                    return item;
                }
        );
    }

    @PostMapping("/wishlist/add")
    @ResponseBody
    public String addWishListItem(@RequestParam Long itemId, @RequestParam int donationAmount) {
        Long userId = wishListService.getCurrentUserId();
        if (userId == null) {
            return "Error: User not authenticated.";
        }

        WishItem itemToAdd = jdbcTemplate.queryForObject(
                "SELECT item_name, item_description, price FROM wish_items WHERE id = ?",
                new Object[]{itemId},
                (rs, rowNum) -> {
                    WishItem wishItem = new WishItem();
                    wishItem.setItemName(rs.getString("item_name"));
                    wishItem.setDescription(rs.getString("item_description"));
                    wishItem.setPrice(rs.getDouble("price"));
                    return wishItem;
                }
        );

        if (itemToAdd == null) {
            return "Error: Item does not exist.";
        }

        boolean success = false;
        String responseMessage;

        try {
            int rowsInserted = jdbcTemplate.update(
                    "INSERT INTO wishlist_items (item_name, item_description, price, user_id, is_reserved) VALUES (?, ?, ?, ?, 0)",
                    itemToAdd.getItemName(), itemToAdd.getDescription(), itemToAdd.getPrice(), userId
            );

            success = rowsInserted > 0;
            responseMessage = success ? "Item added to wishlist successfully!" : "Failed to add item to wishlist.";

            if (donationAmount > 0) {
                int rowsUpdated = jdbcTemplate.update(
                        "UPDATE wish_users SET times_donated = times_donated + 1 WHERE id = ?",
                        userId
                );

                if (rowsUpdated <= 0) {
                    System.out.println("No update occurred for times_donated for id: " + userId);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            responseMessage = "Error: " + e.getMessage();
        }

        return responseMessage;
    }

    @PostMapping("/wishlist/reserve")
    @ResponseBody
    public String toggleReserveItem(@RequestParam Long itemId) {
        WishListItem item = jdbcTemplate.queryForObject(
                "SELECT id, is_reserved FROM wishlist_items WHERE id = ?",
                new Object[]{itemId},
                (rs, rowNum) -> {
                    WishListItem wishListItem = new WishListItem();
                    wishListItem.setId(rs.getLong("id"));
                    wishListItem.setIsReserved(rs.getInt("is_reserved"));
                    return wishListItem;
                }
        );

        if (item == null) {
            return "Error: Item does not exist.";
        }

        int newReserveStatus = item.getIsReserved() == 1 ? 0 : 1;
        int rowsUpdated = jdbcTemplate.update(
                "UPDATE wishlist_items SET is_reserved = ? WHERE id = ?",
                newReserveStatus, itemId
        );

        return (rowsUpdated > 0) ? (newReserveStatus == 1 ? "Reserved" : "Unreserved") : "Error updating item.";
    }

    @PostMapping("/wishlist/unreserve")
    @ResponseBody
    public String unreserveItem(@RequestBody Map<String, Long> request) {
        Long itemId = request.get("itemId");
        Long userId = wishListService.getCurrentUserId();

        boolean success = wishListItemRepository.unreserveItem(itemId);

        return success ? "Item unreserved successfully" : "Error unreserving item";
    }

    @GetMapping("/signup")
    public String showSignupForm(Model model) {
        model.addAttribute("username", getAuthenticatedUsername());
        model.addAttribute("user", new User());
        return "signup";
    }

    @PostMapping("/signup")
    public String registerUser(@ModelAttribute User user, Model model) {
        try {
            System.out.println("Attempting to register user: " + user.getUsername());

            String checkSql = "SELECT COUNT(*) FROM wish_users WHERE username = ?";
            Integer count = jdbcTemplate.queryForObject(checkSql, new Object[]{user.getUsername()}, Integer.class);

            if (count != null && count > 0) {
                model.addAttribute("message", "Username already exists!");
                return "signup";
            }

            user.setEnabled(true);
            String hashedPassword = passwordEncoder.encode(user.getPassword());

            String sql = "INSERT INTO wish_users (username, password, enabled) VALUES (?, ?, ?)";
            int rowsAffected = jdbcTemplate.update(sql, user.getUsername(), hashedPassword, user.isEnabled());

            System.out.println("Rows affected: " + rowsAffected);
            model.addAttribute("message", "User registered successfully!");
            return "redirect:/login";
        } catch (Exception e) {
            model.addAttribute("message", "Error: " + e.getMessage());
            e.printStackTrace();
        }
        return "signup";
    }
}