package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

/**
 * The type User controller.
 */
@RestController
@RequestMapping("/api/user")
public class UserController {
    private static final Logger log = LogManager.getLogger(UserController.class);

    private UserRepository userRepository;

    private CartRepository cartRepository;

    private BCryptPasswordEncoder bCryptPasswordEncoder;


    public UserController(UserRepository userRepository, CartRepository cartRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    /**
     * Find by id response entity.
     *
     * @param id the id
     * @return the response entity
     */
    @GetMapping("/id/{id}")
    public ResponseEntity<User> findById(@PathVariable Long id) {
        log.info("API findById called with id {}", id);
        return ResponseEntity.of(userRepository.findById(id));
    }

    /**
     * Find by user name response entity.
     *
     * @param username the username
     * @return the response entity
     */
    @GetMapping("/{username}")
    public ResponseEntity<User> findByUserName(@PathVariable String username) {
        log.info("API findByUserName called with username {}", username);
        User user = userRepository.findByUsername(username);
        return user == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(user);
    }

    /**
     * Create user response entity.
     *
     * @param createUserRequest the create user request
     * @return the response entity
     */
    @PostMapping("/create")
    public ResponseEntity<User> createUser(@RequestBody CreateUserRequest createUserRequest) {
        log.info("API createUser called");
        User user = new User();
        String username = createUserRequest.getUsername();
        String password = createUserRequest.getPassword();
        user.setUsername(username);
        Cart cart = new Cart();
        cartRepository.save(cart);
        user.setCart(cart);
        if (password != null && (password.length() < 7 ||
                !password.equals(createUserRequest.getConfirmPassword()))) {
            log.error("Error when create user {}", username);
            return ResponseEntity.badRequest().build();
        }
        user.setPassword(bCryptPasswordEncoder.encode(password));
        userRepository.save(user);
        log.info("New user created with name: {}", username);
        return ResponseEntity.ok(user);
    }

}
