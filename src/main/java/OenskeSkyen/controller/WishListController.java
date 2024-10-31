package OenskeSkyen.controller;

import OenskeSkyen.model.User;
import OenskeSkyen.model.WishListItem;
import OenskeSkyen.service.WishListService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/")
public class WishListController {
    private final WishListService wishListService;
    private final JdbcTemplate jdbcTemplate;
    private final PasswordEncoder passwordEncoder; // Inject PasswordEncoder

    public WishListController(WishListService wishListService, JdbcTemplate jdbcTemplate, PasswordEncoder passwordEncoder) {
        this.wishListService = wishListService;
        this.jdbcTemplate = jdbcTemplate;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/")
    public String showIndexPage(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username;

        if (auth != null && auth.isAuthenticated()) {
            username = auth.getName();
            model.addAttribute("username", username);
        } else {

            model.addAttribute("username", "Guest");
        }

        return "index";
    }

    @GetMapping("/login")
    public String login() {
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

    @GetMapping("/add")
    public String addWishListItemForm(Model model) {
        model.addAttribute("wishListItem", new WishListItem());
        return "addWishListItem";
    }

    @PostMapping("/add")
    public String addWishListItem(@ModelAttribute WishListItem wishListItem) {
        wishListService.addWishListItem(wishListItem.getItemName(), wishListItem.getDescription());
        return "redirect:/home";
    }

    @GetMapping("/signup")
    public String showSignupForm(Model model) {
        model.addAttribute("user", new User());
        return "signup";
    }

    @PostMapping("/signup")
    public String registerUser(@ModelAttribute User user, Model model) {
        try {
            System.out.println("Attempting to register user: " + user.getUsername());

            // Check if the user already exists
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