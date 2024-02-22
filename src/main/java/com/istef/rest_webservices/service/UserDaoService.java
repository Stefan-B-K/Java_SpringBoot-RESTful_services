package com.istef.rest_webservices.service;

import com.istef.rest_webservices.entity.User;
import com.istef.rest_webservices.model.UserV2;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserDaoService {

    private static final List<User> userList = new ArrayList<>();
    private static Long userCount = 0L;
//    private static final List<UserV2> userListV2 = new ArrayList<>();
//    private static int userCountV2 = 0;

    static {
        userList.add(new User(++userCount, "Anna Stefanova", LocalDate.of(1999, 2, 22)));
        userList.add(new User(++userCount, "Stef Boris Kozhuh", LocalDate.of(1977, 5, 16)));
        userList.add(new User(++userCount, "Emil Ivanov Todorov", LocalDate.of(1987, 11, 28)));
        userList.add(new User(++userCount, "Milen B. Uzunov", LocalDate.of(1955, 1, 22)));
        userList.add(new User(++userCount, "Teodor Aleksiev", LocalDate.of(1980, 7, 15)));
        userList.add(new User(++userCount, "Vasil Hristov", LocalDate.of(2001, 9, 20)));

//        userListV2.add(new UserV2(++userCountV2, new Name("Anna", "Stefanova"), LocalDate.of(1999, 2, 22)));
//        userListV2.add(new UserV2(++userCountV2, new Name("Stef", "Boris", "Kozhuh"), LocalDate.of(1977, 5, 16)));
//        userListV2.add(new UserV2(++userCountV2, new Name("Emil", "Ivanov", "Todorov"), LocalDate.of(1987, 11, 28)));
//        userListV2.add(new UserV2(++userCountV2, new Name("Milen", "B.", "Uzunov"), LocalDate.of(1955, 1, 22)));
//        userListV2.add(new UserV2(++userCountV2, new Name("Teodor", "Aleksiev"), LocalDate.of(1980, 7, 15)));
//        userListV2.add(new UserV2(++userCountV2, new Name("Vasil", "Hristov"), LocalDate.of(2001, 9, 20)));
    }

    public List<User> findAll() {
        return userList;
    }

    public Optional<User> find(Long id) {
        return userList.stream()
                .filter(user -> user.getId() == id)
                .findFirst();
    }

    public Optional<Long> create(User user) {
        if (Math.random() > 0.5) return Optional.empty();

        user.setId(++userCount);
        userList.add(user);
        return Optional.of(user.getId());
    }

    public boolean deleteBtId(Long id) {
        return userList.removeIf(user -> user.getId() == id);
    }

//    public List<UserV2> findAllV2() {
//        return userListV2;
//    }
//
//    public Optional<UserV2> findV2(int id) {
//        return userListV2.stream()
//                .filter(user -> user.getId() == id)
//                .findFirst();
//    }
}
