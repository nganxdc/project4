package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * The type Order controller test.
 */
public class OrderControllerTest {
    /**
     * The User repository.
     */
    UserRepository userRepository;
    /**
     * The Order repository.
     */
    OrderRepository orderRepository;
    /**
     * The Order controller.
     */
    OrderController orderController;

    /**
     * Sets up.
     */
    @Before
    public void setUp() {
        orderRepository = mock(OrderRepository.class);
        userRepository = mock(UserRepository.class);
        orderController = new OrderController(userRepository, orderRepository);
        Item item = new Item();
        item.setId(1L);
        item.setName("Order name");
        item.setDescription("Order name");
        item.setPrice(BigDecimal.valueOf(4.5));
        List<Item> items = new ArrayList<>();
        items.add(item);
        User user = new User();
        Cart cart = new Cart();
        user.setId(1);
        user.setUsername("NganPV");
        user.setPassword("admin");
        cart.setId(1L);
        cart.setUser(user);
        cart.setItems(items);
        cart.setTotal(BigDecimal.valueOf(4.5));
        user.setCart(cart);
        when(userRepository.findByUsername("NganPV")).thenReturn(user);
    }

    /**
     * Submit.
     */
    @Test
    public void order() {
        ResponseEntity<UserOrder> response = orderController.submit("NganPV");
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, Objects.requireNonNull(response.getBody()).getItems().size());
    }

    /**
     * Submit not found.
     */
    @Test
    public void orderNotFound() {
        ResponseEntity<UserOrder> response = orderController.submit("NGANPV2");
        assertEquals(404, response.getStatusCodeValue());
    }

    /**
     * Gets orders for user.
     */
    @Test
    public void getOrdersForUser() {
        ResponseEntity<List<UserOrder>> orders = orderController.getOrdersForUser("NganPV");
        assertEquals(200, orders.getStatusCodeValue());
    }

    /**
     * Gets orders for user not found.
     */
    @Test
    public void getOrdersForUserNotFound() {
        ResponseEntity<List<UserOrder>> orders = orderController.getOrdersForUser("NGANPV1");
        assertEquals(404, orders.getStatusCodeValue());

    }
}