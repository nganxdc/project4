package com.example.demo.controllers;

import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * The type Item controller test.
 */
public class ItemControllerTest {
    /**
     * The Item repository.
     */
    ItemRepository itemRepository;
    /**
     * The Item controller.
     */
    ItemController itemController;

    /**
     * Sets up.
     */
    @Before
    public void setUp() {
        itemRepository = mock(ItemRepository.class);
        itemController = new ItemController(itemRepository);
        Item item = new Item();
        item.setId(1L);
        item.setName("item name");
        item.setPrice(BigDecimal.valueOf(3.0));
        item.setDescription("Item name");
        when(itemRepository.findAll()).thenReturn(Collections.singletonList(item));
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(itemRepository.findByName("item name")).thenReturn(Collections.singletonList(item));
    }

    /**
     * Gets items.
     */
    @Test
    public void getItemsSuccess() {
        ResponseEntity<List<Item>> response = itemController.getItems();
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    /**
     * Gets item by id.
     */
    @Test
    public void getItemByIdSuccess() {
        ResponseEntity<Item> response = itemController.getItemById(1L);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("item name", Objects.requireNonNull(response.getBody()).getName());
    }

    /**
     * Gets items by name.
     */
    @Test
    public void getItemsByNameSuccess() {
        ResponseEntity<List<Item>> response = itemController.getItemsByName("item name");
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, Objects.requireNonNull(response.getBody()).size());
    }

    /**
     * Gets items by name not found.
     */
    @Test
    public void getItemsByNameNotFound() {
        ResponseEntity<List<Item>> response = itemController.getItemsByName("NAME");
        assertEquals(404, response.getStatusCodeValue());
    }
}