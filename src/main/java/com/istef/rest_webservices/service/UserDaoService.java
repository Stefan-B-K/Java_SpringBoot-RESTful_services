package com.istef.rest_webservices.service;

import com.istef.rest_webservices.model.User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserDaoService {

    private static final List<User> userList = new ArrayList<>();
    private static int userCount = 0;

    static {
        userList.add(new User(++userCount, "Anna", LocalDate.of(1999, 2, 22)));
        userList.add(new User(++userCount, "Stef", LocalDate.of(1977, 5, 16)));
        userList.add(new User(++userCount, "Emil", LocalDate.of(1987, 11, 28)));
        userList.add(new User(++userCount, "Milen", LocalDate.of(1955, 1, 22)));
        userList.add(new User(++userCount, "Teodor", LocalDate.of(1980, 7, 15)));
        userList.add(new User(++userCount, "Vasil", LocalDate.of(2001, 9, 20)));
    }

    public List<User> findAll() {
        return userList;
    }

    public Optional<User> find(int id) {
        return userList.stream()
                .filter(user -> user.getId() == id)
                .findFirst();
    }

    public Optional<Integer> create(User user) {
        if (Math.random() > 0.5) return Optional.empty();

        user.setId(++userCount);
        userList.add(user);
        return Optional.of(user.getId());
    }

    public boolean deleteBtId(int id) {
        return userList.removeIf(user -> user.getId() == id);
    }
}
