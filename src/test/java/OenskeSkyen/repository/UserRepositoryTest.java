package OenskeSkyen.repository;

import OenskeSkyen.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Repository;
import org.springframework.test.context.ActiveProfiles;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("prod")
@Repository

public class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Test
    public void findByUsername() {
        User user = userRepository.findByUsername("Jiozx");
        assertEquals("Jiozx", user.getUsername());
    }
}