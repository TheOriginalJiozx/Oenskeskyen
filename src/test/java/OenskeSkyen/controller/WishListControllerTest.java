package OenskeSkyen.controller;

import OenskeSkyen.model.WishListItem;
import OenskeSkyen.repository.UserRepository;
import OenskeSkyen.service.WishListService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.ui.Model;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("h2")
public class WishListControllerTest {

    @Autowired
    private WishListService wishListService;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testViewWishList() {

        String testUsername = "Test";
        Long userId = userRepository.getUserIdByUsername(testUsername);

        assertEquals(testUsername, userRepository.findByUsername(testUsername).getUsername());

        Model model = mock(Model.class);

        WishListController controller = new WishListController(wishListService, null, null, null, userRepository);
        String result = controller.viewWishList(testUsername, model);

        assertEquals("view", result);

        List<WishListItem> wishListItems = wishListService.getWishListItemsForUser(testUsername);
        when(model.getAttribute("wishListItems")).thenReturn(wishListItems);
        when(model.getAttribute("wishlistOwner")).thenReturn(testUsername);
    }
}