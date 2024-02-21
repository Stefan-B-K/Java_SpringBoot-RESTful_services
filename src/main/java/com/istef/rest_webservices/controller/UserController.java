package com.istef.rest_webservices.controller;

import com.istef.rest_webservices.model.User;
import com.istef.rest_webservices.exception.UserNotFoundException;
import com.istef.rest_webservices.service.UserDaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("users")
public class UserController {

    private final UserDaoService userDaoService;

    public UserController(UserDaoService userDaoService) {
        this.userDaoService = userDaoService;
    }

    @GetMapping
    public List<User> getAll() {
        return userDaoService.findAll();
    }

    @GetMapping("/{id}")
    public User getOne(@PathVariable int id) {
        return userDaoService.findOne(id)
                .orElseThrow(() -> new UserNotFoundException("ID: " + id));
    }

    @PostMapping
    public ResponseEntity<Integer> addOne(@RequestBody User user) {
        Optional<Integer> newUserId = userDaoService.create(user);
        if (newUserId.isEmpty()) return ResponseEntity.status(500).body(null);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newUserId.get())
                .toUri();

        return ResponseEntity.created(location).body(newUserId.get());
    }
}
