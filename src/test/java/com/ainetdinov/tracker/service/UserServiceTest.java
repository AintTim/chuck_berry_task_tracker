package com.ainetdinov.tracker.service;

import com.ainetdinov.tracker.base.BaseTest;
import com.ainetdinov.tracker.configuration.HibernateConfiguration;
import com.ainetdinov.tracker.model.mapper.UserMapper;
import com.ainetdinov.tracker.model.request.UserRequest;
import com.ainetdinov.tracker.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest extends BaseTest {
    UserService userService;
    UserRepository userRepository;

    @BeforeEach
    void setUp() {
        HibernateConfiguration configuration = new HibernateConfiguration(
                postgres.getJdbcUrl(),
                postgres.getUsername(),
                postgres.getPassword());
        userRepository = new UserRepository(configuration.getSessionFactory());
        userService = new UserService(userRepository, UserMapper.INSTANCE, resourceBundle);
    }

    @AfterEach
    void tearDown() {
        userService.deleteEntities();
    }

    @Test
    void should_create_user() {
        var request = UserRequest.builder().username("Admin").password("Password").build();
        userService.createEntity(request);
        var users = userService.getEntities();
        var user = users.getFirst();

        assertEquals(1, users.size());
        assertEquals(request.getUsername(), user.getUsername());
    }

    @Test
    void should_get_user_by_username() {
        var request = createUser(userService, "Anton");
        var user = userService.getUserByUsername(request);

        assertEquals(request.getUsername(), user.getUsername());
    }

    @Test
    void should_return_true_if_user_with_credentials_exists() {
        var request = createUser(userService, "Anton");
        var isFound = userService.validateUserPresence(request);

        assertTrue(isFound);
    }

    @Test
    void should_return_false_if_user_password_does_not_match() {
        var request = createUser(userService, "Anton");
        var updated = request.toBuilder().password("newpassword").build();
        var isFound = userService.validateUserPresence(updated);

        assertFalse(isFound);
    }

    @Test
    void should_return_false_if_user_username_does_not_match() {
        var request = createUser(userService, "Anton");
        var updated = request.toBuilder().username("SomeGuy").build();
        var isFound = userService.validateUserPresence(updated);

        assertFalse(isFound);
    }

    @Test
    void should_return_true_if_username_is_available() {
        var request = createUser(userService, "Anton");
        var updated = request.toBuilder().username("SomeGuy").build();
        var isFound = userService.validateUsernameAvailability(updated);

        assertTrue(isFound);
    }

    @Test
    void should_return_false_if_username_is_taken() {
        var request = createUser(userService, "Anton");
        var isFound = userService.validateUsernameAvailability(request);

        assertFalse(isFound);
    }
}