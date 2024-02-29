package com.istef.rest_webservices.controller;

import com.istef.rest_webservices.dao.PostRepository;
import com.istef.rest_webservices.dao.UserRepository;
import com.istef.rest_webservices.entity.Post;
import com.istef.rest_webservices.entity.User;
import com.istef.rest_webservices.exception.UserNotFoundException;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.Valid;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;


@RestController
@RequestMapping("users-jpa")
public class UserJpaController {

    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public UserJpaController(UserRepository userRepository, PostRepository postRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }


    @GetMapping
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @GetMapping("/{id}")
    public EntityModel<User> getOne(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("ID: " + id));

        EntityModel<User> entityModel = EntityModel.of(user);
        WebMvcLinkBuilder linkBuilder = linkTo(methodOn(this.getClass()).getAll());
        entityModel.add(linkBuilder.withRel("all-users"));
        return entityModel;
    }

    @PostMapping
    @Hidden
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

    @GetMapping("/{userId}/posts")
    public List<Post> getPostsForUser(@PathVariable Long userId) {
       User user = userRepository.findById(userId)
               .orElseThrow(() -> new UserNotFoundException("User ID: " + userId));
       return user.getPosts();
    }

    @PostMapping("/{userId}/posts")
    public ResponseEntity<Long> addPostForUser(@PathVariable Long userId, @RequestBody Post post) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User ID: " + userId));
        post.setUser(user);
        post = postRepository.save(post);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(post.getId())
                .toUri();

        return ResponseEntity.created(location).body(post.getId());
    }
}
