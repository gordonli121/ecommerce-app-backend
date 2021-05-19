package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {

    private OrderController orderController;

    private OrderRepository orderRepository = mock(OrderRepository.class);

    private UserRepository userRepository = mock(UserRepository.class);


    @Before
    public void setUp() {
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "orderRepository", orderRepository);
        TestUtils.injectObjects(orderController, "userRepository", userRepository);
    }

    @Test
    public void submit_order_happy_path() {
        String username = "test1";

        User user = new User();
        Cart cart = new Cart();

        cart.setUser(user);
        Item item1 = new Item();
        Item item2 = new Item();
        List itemList = new ArrayList();
        itemList.add(item1);
        itemList.add(item2);
        cart.setItems(itemList);

        user.setUsername(username);
        user.setCart(cart);

        when(userRepository.findByUsername(username)).thenReturn(user);

        final ResponseEntity<UserOrder> response2 = orderController.submit(username);

        assertNotNull(response2);
        assertEquals(200, response2.getStatusCodeValue());
    }

    @Test
    public void submit_order_fail_path() {
        String username = "test";
        when(userRepository.findByUsername(username)).thenReturn(null);

        final ResponseEntity<UserOrder> response = orderController.submit(username);

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void order_history_happy_path() {
        String username = "test";

        User user = new User();
        user.setUsername(username);

        when(userRepository.findByUsername(username)).thenReturn(user);

        final ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser(username);

        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void order_history_fail_path() {
        String username = "test";

        when(userRepository.findByUsername(username)).thenReturn(null);

        final ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser(username);

        assertEquals(404, response.getStatusCodeValue());
    }
}
