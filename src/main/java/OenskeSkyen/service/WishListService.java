package OenskeSkyen.service;

import OenskeSkyen.model.User;
import OenskeSkyen.model.WishListItem;
import OenskeSkyen.model.Category;
import OenskeSkyen.repository.UserRepository;
import OenskeSkyen.repository.WishListItemRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WishListService {
    private UserRepository userRepository;
    private WishListItemRepository wishListItemRepository;

    public WishListService(UserRepository userRepository, WishListItemRepository wishListItemRepository) {
        this.userRepository = userRepository;
        this.wishListItemRepository = wishListItemRepository;
    }

    public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            User user = userRepository.findByUsername(authentication.getName());
            return user != null ? user.getId() : null;
        }
        return null;
    }

    public List<WishListItem> getWishListItems() {
        Long userId = getCurrentUserId();
        if (userId == null) {
            System.out.println("No user ID found; returning empty list.");
            return List.of();
        }
        try {
            return wishListItemRepository.findByUserId(userId);
        } catch (Exception e) {
            System.out.println("Error fetching wishlist items: " + e.getMessage());
            return List.of();
        }
    }

    public List<Category> getAllCategories() {
        return wishListItemRepository.findAllCategories();
    }

    public List<WishListItem> getWishListItemsForUser(String username) {

        User user = userRepository.findByUsername(username);

        if (user == null) {
            return List.of();
        }

        Long userId = user.getId();

        List<WishListItem> wishListItems = wishListItemRepository.findAll();
        return wishListItems.stream()
                .filter(item -> item.getUserId().equals(userId))
                .collect(Collectors.toList());
    }
}