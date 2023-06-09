package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Objects;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * The type User controller test.
 */
public class UserControllerTest {
  /**
   * The User repository.
   */
  UserRepository userRepository;
  /**
   * The Cart repository.
   */
  CartRepository cartRepository;
  /**
   * The B crypt password encoder.
   */
  BCryptPasswordEncoder bCryptPasswordEncoder;
  /**
   * The User controller.
   */
  UserController userController;

  /**
   * Sets up.
   */
  @Before
  public void setUp() {
    bCryptPasswordEncoder = mock(BCryptPasswordEncoder.class);
    cartRepository = mock(CartRepository.class);
    userRepository = mock(UserRepository.class);
    userController = new UserController(userRepository, cartRepository, bCryptPasswordEncoder);
    User user = new User();
    user.setId(1);
    user.setUsername("NganPV");
    user.setPassword("6543210");
    user.setCart(new Cart());
    when(userRepository.findByUsername("NganPV")).thenReturn(user);
    when(userRepository.findById(1L)).thenReturn(Optional.of(user));
    when(bCryptPasswordEncoder.encode("6543210")).thenReturn("654321");
  }

  /**
   * Find by id.
   */
  @Test
  public void findByIdSuccess() {
    ResponseEntity<User> response = userController.findById(1L);
    assertEquals(200, response.getStatusCodeValue());
    assertNotNull(response.getBody());
    assertEquals(1, response.getBody().getId());
  }

  /**
   * Find by id not found.
   */
  @Test
  public void findByIdNotFound() {
    ResponseEntity<User> response = userController.findById(3L);
    assertEquals(404, response.getStatusCodeValue());
  }

  /**
   * Find by username.
   */
  @Test
  public void findByUserNameSuccess() {
    ResponseEntity<User> response = userController.findByUserName("NganPV");
    assertEquals(200, response.getStatusCodeValue());
    assertNotNull(response.getBody());
    assertEquals("NganPV", response.getBody().getUsername());
  }

  /**
   * Find by username not found.
   */
  @Test
  public void findByUserNameNotFound() {
    ResponseEntity<User> response = userController.findByUserName("abc");
    assertEquals(404, response.getStatusCodeValue());
  }


  /**
   * Create user success.
   */
  @Test
  public void createUserSuccess() {
    CreateUserRequest request = new CreateUserRequest();
    request.setUsername("NganPV");
    request.setPassword("6543210");
    request.setConfirmPassword("6543210");
    ResponseEntity<User> response = userController.createUser(request);
    assertEquals(200, response.getStatusCodeValue());
    User user = response.getBody();
    assertNotNull(user);
    assertEquals(0, user.getId());
    assertEquals("654321", user.getPassword());
    assertEquals("NganPV", user.getUsername());
  }

  /**
   * Create user password not match.
   */
  @Test
  public void createUserPasswordNotMatch() {
    CreateUserRequest request = new CreateUserRequest();
    request.setUsername("NganPV");
    request.setPassword("admin");
    request.setConfirmPassword("Admin");
    ResponseEntity<User> response = userController.createUser(request);
    assertEquals(400, response.getStatusCodeValue());
  }

  /**
   * Create user with password policy not correct.
   */
  @Test
  public void createUserWithPasswordPolicyNotCorrect() {
    CreateUserRequest request = new CreateUserRequest();
    request.setUsername("NganPV");
    request.setPassword("admin");
    request.setConfirmPassword("admin");
    ResponseEntity<User> response = userController.createUser(request);
    assertEquals(400, response.getStatusCodeValue());
  }


}