package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {

    private ItemController itemController;

    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setUp() {
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepository);
    }

    @Test
    public void verify_get_items() {
        final ResponseEntity<List<Item>> response = itemController.getItems();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void verify_get_item_by_id() {
        Long id = 1L;
        Item item = new Item();

        when(itemRepository.findById(id)).thenReturn(Optional.of(item));

        final ResponseEntity<Item> response = itemController.getItemById(id);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void verify_get_item_by_name() {
        String name = "item";
        Item item1 = new Item();
        item1.setName(name);
        Item item2 = new Item();
        item2.setName(name);

        List itemList = new ArrayList();
        itemList.add(item1);
        itemList.add(item2);

        when(itemRepository.findByName(name)).thenReturn(itemList);

        final ResponseEntity<List<Item>> response = itemController.getItemsByName(name);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void get_item_by_name_null() {
        String name = "test";
        when(itemRepository.findByName(name)).thenReturn(null);
        final ResponseEntity<List<Item>> response = itemController.getItemsByName(name);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void get_item_by_name_empty() {
        String name = "test";
        when(itemRepository.findByName(name)).thenReturn(new ArrayList<Item>());
        final ResponseEntity<List<Item>> response = itemController.getItemsByName(name);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }
}
