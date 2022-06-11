package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.Collection;

@Slf4j
@RestController
public class UserController {
    private final InMemoryUserStorage storage;
    private final UserService service;

    @Autowired
    public UserController(InMemoryUserStorage storage, UserService service) {
        this.storage = storage;
        this.service = service;
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
    public User refresh(@RequestBody User user) {
        return storage.modifyUser(user);
    }

    @DeleteMapping("/users")
    public User removeUser(@RequestBody User user) {
        return storage.deleteUser(user);
    }

    @GetMapping("/users/{id}")
    public User getUserById(@PathVariable int id) {
        return storage.getUserById(id);
    }

    //--------------------------Переменная пути--------------------------------------------
    @PutMapping("/users/{id}/friends/{friendId}")
    public void setFriendship(@PathVariable int id, @PathVariable int friendId) {
        service.setFriendship(id, friendId);
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public void breakFriendship(@PathVariable int id, @PathVariable int friendId) {
        service.breakFriendship(id, friendId);
    }

    @GetMapping("/users/{id}/friends")
    public Collection<User> getAllFriends(@PathVariable int id) {
        return service.getAllFriendsId(id);
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public Collection<User> getCommonFriendsIds(@PathVariable int id, @PathVariable int otherId) {
        return service.getCommonFriends(id, otherId);
    }
}
