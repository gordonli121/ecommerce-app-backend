package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    private UserController userController;

    private UserRepository userRepository = mock(UserRepository.class);

    private CartRepository cartRepository = mock(CartRepository.class);

    private BCryptPasswordEncoder bCryptPasswordEncoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setUp() {
        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", userRepository);
        TestUtils.injectObjects(userController, "cartRepository", cartRepository);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", bCryptPasswordEncoder);
    }

    @Test
    public void create_user_happy_path() throws Exception {
        String username = "test1";
        String password = "testPassword";

        when(bCryptPasswordEncoder.encode(password)).thenReturn("secretHash");

        CreateUserRequest r = new CreateUserRequest();
        r.setUsername(username);
        r.setPassword(password);
        r.setConfirmPassword(password);

        final ResponseEntity<User> response = userController.createUser(r);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User user = response.getBody();
        assertNotNull(user);
        assertEquals(0, user.getId());
        assertEquals(username, user.getUsername());
        assertEquals("secretHash", user.getPassword());
    }

    @Test
    public void create_user_short_password() throws Exception {
        String username = "test2";
        String password = "123456";

        CreateUserRequest r = new CreateUserRequest();
        r.setUsername(username);
        r.setPassword(password);
        r.setConfirmPassword(password);

        final ResponseEntity<User> response = userController.createUser(r);

        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    public void create_user_mismatch_password() throws Exception {
        String username = "test3";
        String password = "1234567";

        CreateUserRequest r = new CreateUserRequest();
        r.setUsername(username);
        r.setPassword(password);
        r.setConfirmPassword(password+"8");

        final ResponseEntity<User> response = userController.createUser(r);

        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    public void findByUsername_happy_path() throws Exception {
        String username = "test4";
        String password = "1234567";

        when(bCryptPasswordEncoder.encode(password)).thenReturn("secretHash");

        CreateUserRequest r = new CreateUserRequest();
        r.setUsername(username);
        r.setPassword(password);
        r.setConfirmPassword(password);

        final ResponseEntity<User> response1 = userController.createUser(r);

        User userBody = response1.getBody();

        when(userRepository.findByUsername(userBody.getUsername())).thenReturn(userBody);
        final ResponseEntity<User> response2 = userController.findByUserName(username);

        assertNotNull(response2);
        assertEquals(200, response2.getStatusCodeValue());
    }

    @Test
    public void findByUsername_fail_path() throws Exception {
        final ResponseEntity<User> response = userController.findByUserName("invalid");

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void findById_happy_path() throws Exception {
        String username = "test5";
        String password = "1234567";

        when(bCryptPasswordEncoder.encode(password)).thenReturn("secretHash");

        CreateUserRequest r = new CreateUserRequest();
        r.setUsername(username);
        r.setPassword(password);
        r.setConfirmPassword(password);

        final ResponseEntity<User> response1 = userController.createUser(r);

        User userBody = response1.getBody();

        when(userRepository.findById(userBody.getId())).thenReturn(Optional.of(userBody));
        final ResponseEntity<User> response2 = userController.findById(userBody.getId());

        assertNotNull(response2);
        assertEquals(200, response2.getStatusCodeValue());
    }

    @Test
    public void findByID_fail_path() throws Exception {
        final ResponseEntity<User> response = userController.findById(12345L);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }
}
