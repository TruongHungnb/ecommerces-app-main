package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;


class ItemControllerTest {
    @Autowired
    private ItemController itemController;

    @BeforeEach
    public void setUp() {
        //don't use mock as we are covering all layers
//        itemController = new ItemController();
//        TestUtils.injectObjects(itemController,"itemRepository",itemRepo);
    }


    @Test
    void getItems() {
        //test getItems
        ResponseEntity<List<Item>> r = itemController.getItems();
        assertEquals(HttpStatus.OK,r.getStatusCode());
        List<Item> items = r.getBody();
        assertEquals(2,items.size());
        assertEquals("Round Widget",items.get(0).getName());
        assertEquals("A widget that is square",items.get(0).getDescription());

        //test getItemById

        ResponseEntity<Item> r1 = itemController.getItemById(1l);
        assertEquals(HttpStatus.OK,r.getStatusCode());
        Item item1 = r1.getBody();
        assertEquals("Round Widget",item1.getName());
        assertEquals("A widget that is round",item1.getDescription());

        //void getItemsByName
        ResponseEntity<List<Item>> r2 = itemController.getItemsByName("Square Widget");
        assertEquals(HttpStatus.OK,r2.getStatusCode());
        List<Item> items2 = r.getBody();
        assertEquals("A widget that is square",items.get(0).getDescription());
        assertEquals(2,items.get(0).getId());
    }

    @Test
    void getItemById() {
        ResponseEntity<Item> r = itemController.getItemById(1l);
        assertEquals(HttpStatus.OK,r.getStatusCode());
        Item item = r.getBody();
        assertEquals("Round Widget",item.getName());
        assertEquals("A widget that is round",item.getDescription());
    }

    @Test
    void getItemsByName() {
        ResponseEntity<List<Item>> r = itemController.getItemsByName("Square Widget");
        assertEquals(HttpStatus.OK,r.getStatusCode());
        List<Item> items = r.getBody();
        assertEquals("A widget that is square",items.get(0).getDescription());
        assertEquals(2,items.get(0).getId());
    }
}