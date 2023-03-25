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
import static org.junit.Assert.assertNotNull;
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
     * Submit success.
     */
    @Test
    public void submitSuccess() {
        ResponseEntity<UserOrder> response = orderController.submit("NganPV");
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getItems().size());
    }

    /**
     * Submit not found by username.
     */
    @Test
    public void submitNotFoundByUserName() {
        ResponseEntity<UserOrder> response = orderController.submit("NGANPV2");
        assertEquals(404, response.getStatusCodeValue());
    }

    /**
     * Gets orders by username.
     */
    @Test
    public void getOrdersByUserName() {
        ResponseEntity<List<UserOrder>> orders = orderController.getOrdersForUser("NganPV");
        assertEquals(200, orders.getStatusCodeValue());
        assertEquals(0, Objects.requireNonNull(orders.getBody()).size());
    }

    /**
     * Gets orders for user not exist.
     */
    @Test
    public void getOrdersByUserNameNotExist() {
        ResponseEntity<List<UserOrder>> orders = orderController.getOrdersForUser("NGANPV1");
        assertEquals(404, orders.getStatusCodeValue());

    }
}