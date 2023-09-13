package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserControllerTest {

    private UserController userController;
    private UserRepository userRepo = mock(UserRepository.class);
    private CartRepository cartRepo = mock(CartRepository.class);
    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);
    @BeforeEach
    public  void setUp() {
        userController = new UserController();
        //bean setup
        TestUtils.injectObjects(userController,"userRepository",userRepo);
        TestUtils.injectObjects(userController,"cartRepository",cartRepo);
        TestUtils.injectObjects(userController,"bCryptPasswordEncoder",encoder);
        //mock setup
        when(encoder.encode("testPassword")).thenReturn("hashedTestPassword");

    }

    @Test
    void create_user_happy_path() {
       //create user
        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("test");
        r.setPassword("testPassword");
        r.setConfirmPassword("testPassword");
        ResponseEntity<User> response = userController.createUser(r);
        assertNotNull(response);
        assertEquals(HttpStatus.OK,response.getStatusCode());
        User u = response.getBody();
        assertEquals(0,u.getId());
        assertEquals("test",u.getUsername());
        assertEquals("hashedTestPassword",u.getPassword());
        //get user by id
        ResponseEntity<User> ru1 = userController.findById(u.getId());
        assertEquals(0,u.getId());
        assertEquals("test",u.getUsername());
        assertEquals("hashedTestPassword",u.getPassword());
        // get user by name
        ResponseEntity<User> ru2 = userController.findByUserName(u.getUsername());
        assertEquals(0,u.getId());
        assertEquals("test",u.getUsername());
        assertEquals("hashedTestPassword",u.getPassword());

    }

    @Test
    void create_user_sad_path() {
        //create user
        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("test");
        r.setPassword("testPassword");
        r.setConfirmPassword("wrongConfirmPassword");
        ResponseEntity<User> response = userController.createUser(r);
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST,response.getStatusCode());

    }



}