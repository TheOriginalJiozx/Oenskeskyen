package OenskeSkyen.service;

import OenskeSkyen.model.User;
import OenskeSkyen.model.WishListItem;
import OenskeSkyen.model.Category;
import OenskeSkyen.repository.UserRepository;
import OenskeSkyen.repository.WishListItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class WishListServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private WishListItemRepository wishListItemRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private WishListService wishListService;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks(this);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testUser");

        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("testUser");
        when(userRepository.findByUsername("testUser")).thenReturn(mockUser);

        WishListItem item1 = new WishListItem();
        item1.setId(1L);
        item1.setItemName("Item 1");
        item1.setUserId(1L);

        WishListItem item2 = new WishListItem();
        item2.setId(2L);
        item2.setItemName("Item 2");
        item2.setUserId(1L);

        when(wishListItemRepository.findByUserId(1L)).thenReturn(List.of(item1, item2));

        Category mockCategory = mock(Category.class);
        when(mockCategory.getId()).thenReturn(1L);
        when(mockCategory.getName()).thenReturn("Category 1");
        when(wishListItemRepository.findAllCategories()).thenReturn(List.of(mockCategory));

        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void testGetWishListItems() {

        when(authentication.isAuthenticated()).thenReturn(true);

        List<WishListItem> items = wishListService.getWishListItems();

        assertEquals(2, items.size(), "Should return two wishlist items for the mock user");
        assertEquals("Item 1", items.get(0).getItemName());
        assertEquals("Item 2", items.get(1).getItemName());
    }

    @Test
    void testGetCurrentUserId() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn("testUser");

        SecurityContextHolder.setContext(securityContext);
        Long userId = wishListService.getCurrentUserId();
        assertEquals(1L, userId, "User ID should match the mocked user ID");
    }

    @Test
    void testGetWishListItemsForUser() {

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn("testUser");

        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("testUser");
        when(userRepository.findByUsername("testUser")).thenReturn(mockUser);

        WishListItem item1 = new WishListItem();
        item1.setId(1L);
        item1.setItemName("Item 1");
        item1.setUserId(1L);

        WishListItem item2 = new WishListItem();
        item2.setId(2L);
        item2.setItemName("Item 2");
        item2.setUserId(1L);

        when(wishListItemRepository.findAll()).thenReturn(List.of(item1, item2));

        SecurityContextHolder.setContext(securityContext);

        List<WishListItem> items = wishListService.getWishListItemsForUser("testUser");

        assertEquals(2, items.size(), "Should return two wishlist items for the user 'testUser'");
        assertEquals("Item 1", items.get(0).getItemName());
        assertEquals("Item 2", items.get(1).getItemName());
    }

    @Test
    void testGetAllCategories() {

        List<Category> categories = wishListService.getAllCategories();

        assertEquals(1, categories.size(), "Should return one category in the mock repository");
        assertEquals("Category 1", categories.get(0).getName());
    }
}