package ru.mytumanov.testapp;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserRepository repository;

    @GetMapping("/byId")
    public UserDto getUserById(@RequestParam UUID id) {
        return new UserDto();
    }

    @GetMapping
    public List<UserDto> getUsers() {
        return new ArrayList<>(repository.getUsers());
    }
}
