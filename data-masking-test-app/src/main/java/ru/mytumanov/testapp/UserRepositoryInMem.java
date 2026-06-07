package ru.mytumanov.testapp;

import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class UserRepositoryInMem implements UserRepository {
    private final static Map<UUID, UserDto> users = new HashMap<>();

    //    init
    static {
        UserDto user1 = new UserDto();
        user1.setId(UUID.randomUUID());
        user1.setEmail("mymail@mail.ru");
        user1.setPhoneNumber("+79254719928");

        UserDto user2 = new UserDto();
        user2.setId(UUID.randomUUID());
        user2.setEmail("mymaillong@gmail.com");
        user2.setPhoneNumber("+7(925)4719928");

        UserDto user3 = new UserDto();
        user3.setId(UUID.randomUUID());
        user3.setEmail("mt@mail.ru");
        user3.setPhoneNumber("+719254719928");

        users.put(user1.getId(), user1);
        users.put(user2.getId(), user2);
        users.put(user3.getId(), user3);
    }

    @Override
    public UserDto getUserById(UUID id) {
        if (users.containsKey(id)) {
            return users.get(id);
        }

        throw new RuntimeException("User not found");
    }

    @Override
    public Collection<UserDto> getUsers() {
        return users.values();
    }
}
