package com.istef.rest_webservices.controller;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.istef.rest_webservices.model.User;
import com.istef.rest_webservices.exception.UserNotFoundException;
import com.istef.rest_webservices.model.UserV2;
import com.istef.rest_webservices.service.UserDaoService;
import jakarta.validation.Valid;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

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


    // Versioning examples
    @GetMapping("/v2/{id}")
    public UserV2 getOneV2(@PathVariable int id) {
        return userDaoService.findV2(id)
                .orElseThrow(() -> new UserNotFoundException("ID: " + id));
    }

    @GetMapping(value = "/{id}", params = "v2")
    public UserV2 getOneParamV2(@PathVariable int id) {
        return userDaoService.findV2(id)
                .orElseThrow(() -> new UserNotFoundException("ID: " + id));
    }

    @GetMapping(value = "/{id}", headers = "X-API-Version=2")
    public UserV2 getOneHeaderV2(@PathVariable int id) {
        return userDaoService.findV2(id)
                .orElseThrow(() -> new UserNotFoundException("ID: " + id));
    }

    @GetMapping(value = "/{id}", produces = "application/app-v2+json")
    public UserV2 getOneAcceptHeaderV2(@PathVariable int id) {
        return userDaoService.findV2(id)
                .orElseThrow(() -> new UserNotFoundException("ID: " + id));
    }


    // HATEOAS
    @GetMapping("/hateoas/{id}")
    public EntityModel<User> getOneHateoas(@PathVariable int id) {
        User user = userDaoService.find(id)
                .orElseThrow(() -> new UserNotFoundException("ID: " + id));

        EntityModel<User> entityModel = EntityModel.of(user);
        WebMvcLinkBuilder linkBuilder = linkTo(methodOn(this.getClass()).getAll());
        entityModel.add(linkBuilder.withRel("all-users"));
        return entityModel;

    }


    // Filtering JSON
    @GetMapping("/v2")
    public MappingJacksonValue getAllV2() {
        List<UserV2> userV2List = userDaoService.findAllV2();
        return getMappingJacksonValue(userV2List, "");
    }

    @GetMapping("/v2/filter")
    public MappingJacksonValue getAllV2Filter() {
        List<UserV2> userV2List = userDaoService.findAllV2();
        return getMappingJacksonValue(userV2List, "middleName");
    }

    private static MappingJacksonValue getMappingJacksonValue(List<UserV2> userV2List, String... hiddenProps) {
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.serializeAllExcept(hiddenProps);
        FilterProvider filters = new SimpleFilterProvider().addFilter("NoMiddleName", filter);
        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(userV2List);
        mappingJacksonValue.setFilters(filters);
        return mappingJacksonValue;
    }


}
