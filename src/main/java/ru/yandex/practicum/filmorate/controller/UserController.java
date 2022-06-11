package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.Collection;

@Slf4j
@RestController
public class UserController {
    private final InMemoryUserStorage storage;

    @Autowired
    public UserController(InMemoryUserStorage storage) {
        this.storage = storage;
    }

    @GetMapping("/users")
    public Collection<User> findAll() {
        return storage.getAll();
    }

    @PostMapping("/users")
    public User create(@RequestBody User user) throws ValidationException {
        return storage.addUser(user);
    }

    @PutMapping("/users")
    public User refresh(@RequestBody User user) throws ValidationException {
        return storage.modifyUser(user);
    }

    @DeleteMapping("/users")
    public User removeUser(@RequestBody User user) {
        return storage.deleteUser(user);
    }
}
