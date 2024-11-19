package OenskeSkyen.service;

import OenskeSkyen.model.User;
import OenskeSkyen.model.WishListItem;
import OenskeSkyen.model.Category;
import OenskeSkyen.repository.UserRepository;
import OenskeSkyen.repository.WishListItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WishListService {
    // Repository til at hente brugerdata
    @Autowired
    private UserRepository userRepository;

    // Repository til at hente ønskeliste-elementer
    private WishListItemRepository wishListItemRepository;

    // Konstruktor, der initialiserer begge repository-klasser
    public WishListService(UserRepository userRepository, WishListItemRepository wishListItemRepository) {
        this.userRepository = userRepository;
        this.wishListItemRepository = wishListItemRepository;
    }

    // Henter ID'et for den aktuelt autentificerede bruger
    public Long getCurrentUserId() {
        // Henter autentificeringen fra SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Hvis brugeren er autentificeret, hentes brugerens ID
        if (authentication != null && authentication.isAuthenticated()) {
            User user = userRepository.findByUsername(authentication.getName());
            return user != null ? user.getId() : null; // Returnerer brugerens ID eller null hvis ikke fundet
        }
        return null; // Returnerer null hvis ikke autentificeret
    }

    // Henter alle ønskeliste-elementer for den aktuelle bruger
    public List<WishListItem> getWishListItems() {
        // Henter den aktuelle brugers ID
        Long userId = getCurrentUserId();

        // Hvis ingen bruger-ID findes, returneres en tom liste
        if (userId == null) {
            System.out.println("No user ID found; returning empty list.");
            return List.of();
        }

        // Forsøger at hente ønskeliste-elementer for den fundne bruger
        try {
            return wishListItemRepository.findByUserId(userId);
        } catch (Exception e) {
            // Håndterer eventuelle fejl ved at returnere en tom liste
            System.out.println("Error fetching wishlist items: " + e.getMessage());
            return List.of();
        }
    }

    // Henter alle kategorier af ønskeliste-elementer
    public List<Category> getAllCategories() {
        return wishListItemRepository.findAllCategories();
    }

    // Henter ønskeliste-elementer for en specifik bruger baseret på brugernavn
    public List<WishListItem> getWishListItemsForUser(String username) {
        // Henter brugerobjektet for det angivne brugernavn
        User user = userRepository.findByUsername(username);

        // Hvis brugeren ikke findes, returneres en tom liste
        if (user == null) {
            return List.of();
        }

        // Henter brugerens ID
        Long userId = user.getId();

        // Henter alle ønskeliste-elementer og filtrerer dem efter brugerens ID
        List<WishListItem> wishListItems = wishListItemRepository.findAll();
        return wishListItems.stream()
                .filter(item -> item.getUserId().equals(userId)) // Filtrerer ønskeliste-elementer for at matche brugerens ID
                .collect(Collectors.toList()); // Samler de filtrerede elementer i en liste
    }
}
