package ru.mytumanov.testapp;

import java.util.Collection;
import java.util.UUID;

public interface UserRepository {
    UserDto getUserById(UUID id);

    Collection<UserDto> getUsers();
}
