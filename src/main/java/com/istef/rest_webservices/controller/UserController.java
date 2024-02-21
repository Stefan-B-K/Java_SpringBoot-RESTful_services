package com.istef.rest_webservices.controller;

import com.istef.rest_webservices.model.User;
import com.istef.rest_webservices.exception.UserNotFoundException;
import com.istef.rest_webservices.service.UserDaoService;
import jakarta.validation.Valid;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@RestController
@RequestMapping("users")
public class UserController {

    private final UserDaoService userDaoService;
    private final MessageSource messageSource;

    public UserController(UserDaoService userDaoService, MessageSource messageSource) {

        this.userDaoService = userDaoService;
        this.messageSource = messageSource;
    }

    @GetMapping
    public List<User> getAll() {
        return userDaoService.findAll();
    }

    @GetMapping("/{id}")
    public User getOne(@PathVariable int id) {
        return userDaoService.find(id)
                .orElseThrow(() -> new UserNotFoundException("ID: " + id));
    }

    @PostMapping
    public ResponseEntity<Integer> addOne(@Valid @RequestBody User user) {
        Optional<Integer> newUserId = userDaoService.create(user);
        if (newUserId.isEmpty()) return ResponseEntity.status(500).body(null);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newUserId.get())
                .toUri();

        return ResponseEntity.created(location).body(newUserId.get());
    }

    @DeleteMapping("/{id}")
    public Integer deleteOne(@PathVariable int id) {
        if (!userDaoService.deleteBtId(id)) throw new UserNotFoundException("ID: " + id);
        return 1;
    }

    // i18n example
    @GetMapping("hello-i18n")
    public String greetI18n() {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage("good.morning.message", null, "Default Greeting", locale);
    }

    // Versioning example
}
