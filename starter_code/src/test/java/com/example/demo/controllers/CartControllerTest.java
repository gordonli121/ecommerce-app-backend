package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {

    private CartController cartController;

    private UserRepository userRepository = mock(UserRepository.class);

    private CartRepository cartRepository = mock(CartRepository.class);

    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setUp() {
        cartController = new CartController();
        TestUtils.injectObjects(cartController, "userRepository", userRepository);
        TestUtils.injectObjects(cartController, "cartRepository", cartRepository);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepository);
    }

    @Test
    public void addToCart_happy_path() {
        String username = "test";
        Long id = 1L;
        int quantity = 1;
        BigDecimal price = new BigDecimal(1.25);
        ModifyCartRequest r = new ModifyCartRequest();
        r.setUsername(username);
        r.setItemId(id);
        r.setQuantity(quantity);

        User user = new User();
        user.setUsername(username);

        Item item = new Item();
        item.setId(id);
        item.setPrice(price);

        Cart cart = new Cart();
        cart.setUser(user);
        cart.setItems(new ArrayList<>());
        cart.setTotal(new BigDecimal(0));

        user.setCart(cart);

        when(userRepository.findByUsername(username)).thenReturn(user);
        when(itemRepository.findById(id)).thenReturn(Optional.of(item));

        final ResponseEntity<Cart> response = cartController.addTocart(r);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void addToCart_no_user() {
        String username = "test";
        Long id = 1L;
        int quantity = 1;
        BigDecimal price = new BigDecimal(1.25);
        ModifyCartRequest r = new ModifyCartRequest();
        r.setUsername(username);
        r.setItemId(id);
        r.setQuantity(quantity);

        when(userRepository.findByUsername(username)).thenReturn(null);

        final ResponseEntity<Cart> response = cartController.addTocart(r);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void addToCart_no_item() {
        String username = "test";
        Long id = 1L;
        int quantity = 1;
        ModifyCartRequest r = new ModifyCartRequest();
        r.setUsername(username);
        r.setItemId(id);
        r.setQuantity(quantity);

        User user = new User();
        user.setUsername(username);

        when(userRepository.findByUsername(username)).thenReturn(user);
        when(itemRepository.findById(id)).thenReturn(Optional.empty());

        final ResponseEntity<Cart> response = cartController.addTocart(r);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void removeFromCart_happy_path() {
        String username = "test";
        Long id = 1L;
        int quantity = 1;
        BigDecimal price = new BigDecimal(1.25);
        ModifyCartRequest r = new ModifyCartRequest();
        r.setUsername(username);
        r.setItemId(id);
        r.setQuantity(quantity);

        User user = new User();
        user.setUsername(username);

        Item item = new Item();
        item.setId(id);
        item.setPrice(price);

        Cart cart = new Cart();
        cart.setUser(user);
        cart.setItems(new ArrayList<>());
        cart.setTotal(new BigDecimal(0));

        user.setCart(cart);

        when(userRepository.findByUsername(username)).thenReturn(user);
        when(itemRepository.findById(id)).thenReturn(Optional.of(item));

        final ResponseEntity<Cart> response = cartController.removeFromcart(r);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void removeFromCart_no_user() {
        String username = "test";
        Long id = 1L;
        int quantity = 1;
        BigDecimal price = new BigDecimal(1.25);
        ModifyCartRequest r = new ModifyCartRequest();
        r.setUsername(username);
        r.setItemId(id);
        r.setQuantity(quantity);

        when(userRepository.findByUsername(username)).thenReturn(null);

        final ResponseEntity<Cart> response = cartController.removeFromcart(r);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());

    }

    @Test
    public void removeFromCart_no_item() {
        String username = "test";
        Long id = 1L;
        int quantity = 1;
        ModifyCartRequest r = new ModifyCartRequest();
        r.setUsername(username);
        r.setItemId(id);
        r.setQuantity(quantity);

        User user = new User();
        user.setUsername(username);

        when(userRepository.findByUsername(username)).thenReturn(user);
        when(itemRepository.findById(id)).thenReturn(Optional.empty());

        final ResponseEntity<Cart> response = cartController.removeFromcart(r);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());

    }
}
