package com.istef.rest_webservices.controller;

import com.istef.rest_webservices.dao.UserRepository;
import com.istef.rest_webservices.entity.User;
import com.istef.rest_webservices.exception.UserNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;


@RestController
@RequestMapping("users-jpa")
public class UserJpaController {

    private final UserRepository userRepository;

    public UserJpaController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @GetMapping("/{id}")
    public User getOne(@PathVariable Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("ID: " + id));
    }

    @PostMapping
    public ResponseEntity<Long> addOne(@Valid @RequestBody User user) {
        Long newUserId = userRepository.save(user).getId();

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newUserId)
                .toUri();

        return ResponseEntity.created(location).body(newUserId);
    }

    @DeleteMapping("/{id}")
    public void deleteOne(@PathVariable Long id) {
        userRepository.deleteById(id);
    }
}
