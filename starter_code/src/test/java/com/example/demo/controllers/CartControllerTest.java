package com.example.demo.controllers;

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
import java.util.Objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * The type Cart controller test.
 */
public class CartControllerTest {
    /**
     * The User repository.
     */
    UserRepository userRepository;
    /**
     * The Cart repository.
     */
    CartRepository cartRepository;
    /**
     * The Item repository.
     */
    ItemRepository itemRepository;
    /**
     * The Cart controller.
     */
    CartController cartController;

    /**
     * Sets up.
     */
    @Before
    public void setUp() {
        cartRepository = mock(CartRepository.class);
        userRepository = mock(UserRepository.class);
        itemRepository = mock(ItemRepository.class);
        cartController = new CartController(userRepository, cartRepository, itemRepository);
        User user = new User();
        user.setId(1);
        user.setUsername("nganpv");
        user.setPassword("admin");
        user.setCart(new Cart());
        when(userRepository.findByUsername("nganpv")).thenReturn(user);
        Item item = new Item();
        item.setId(1L);
        item.setName("cart name");
        item.setDescription("Cart name");
        item.setPrice(BigDecimal.valueOf(5.5));
        when(itemRepository.findById(1L)).thenReturn(java.util.Optional.of(item));
    }


    /**
     * Add cart ok.
     */
    @Test
    public void addCartOk() {
        ModifyCartRequest request = new ModifyCartRequest();
        request.setItemId(1);
        request.setUsername("nganpv");
        request.setQuantity(1);
        ResponseEntity<Cart> response = cartController.addTocart(request);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(BigDecimal.valueOf(5.5), Objects.requireNonNull(response.getBody()).getTotal());
    }

    /**
     * Add cart not found username.
     */
    @Test
    public void addCartNotFoundUsername() {
        ModifyCartRequest request = new ModifyCartRequest();
        request.setItemId(1);
        request.setUsername("nganpv1");
        request.setQuantity(1);
        ResponseEntity<Cart> response = cartController.addTocart(request);
        assertEquals(404, response.getStatusCodeValue());
    }

    /**
     * Add cart not found item id.
     */
    @Test
    public void addCartNotFoundItemId() {
        ModifyCartRequest request = new ModifyCartRequest();
        request.setItemId(2);
        request.setUsername("nganpv");
        request.setQuantity(1);
        ResponseEntity<Cart> response = cartController.addTocart(request);
        assertEquals(404, response.getStatusCodeValue());
    }

    /**
     * Remove from cart.
     */
    @Test
    public void removeFromCart() {
        ModifyCartRequest request = new ModifyCartRequest();
        request.setItemId(1);
        request.setUsername("nganpv");
        request.setQuantity(3);
        cartController.addTocart(request);
        request = new ModifyCartRequest();
        request.setItemId(1);
        request.setUsername("nganpv");
        request.setQuantity(1);
        ResponseEntity<Cart> response = cartController.removeFromcart(request);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(BigDecimal.valueOf(11.0), Objects.requireNonNull(response.getBody()).getTotal());

    }

    /**
     * Remove from cart not found user.
     */
    @Test
    public void removeFromCartNotFoundUser() {
        ModifyCartRequest request = new ModifyCartRequest();
        request.setItemId(1);
        request.setUsername("nganpv1");
        request.setQuantity(1);
        ResponseEntity<Cart> response = cartController.removeFromcart(request);
        assertEquals(404, response.getStatusCodeValue());
    }

    /**
     * Remove from cart not found item id.
     */
    @Test
    public void removeFromCartNotFoundItemId() {
        ModifyCartRequest request = new ModifyCartRequest();
        request.setItemId(2);
        request.setUsername("nganpv");
        request.setQuantity(1);
        ResponseEntity<Cart> response = cartController.removeFromcart(request);
        assertEquals(404, response.getStatusCodeValue());
    }
}