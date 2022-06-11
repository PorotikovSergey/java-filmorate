package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    public User refresh(@RequestBody User user) throws ValidationException {
        return storage.modifyUser(user);
    }

    @DeleteMapping("/users")
    public User removeUser(@RequestBody User user) {
        return storage.deleteUser(user);
    }

    //--------------------------Переменная пути--------------------------------------------
    @PutMapping("/users/{id}/friends/{friendId}")
    public void setFriendship(@PathVariable int id, @PathVariable int friendId) {
        service.setFriendship(storage.getUserById(id), storage.getUserById(friendId));
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public void breakFriendship(@PathVariable int id, @PathVariable int friendId) {
        service.breakFriendship(storage.getUserById(id), storage.getUserById(friendId));
    }

    @GetMapping("/users/{id}/friends")
    public Collection<Integer> getAllFriendsIds(@PathVariable int id) {
        return service.getAllFriendsId(storage.getUserById(id));
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public Collection<Integer> getCommonFriendsIds(@PathVariable int id, @PathVariable int otherId) {
        Set<Integer> commonFriends = new HashSet<>(service.getAllFriendsId(storage.getUserById(id)));
        commonFriends.retainAll(service.getAllFriendsId(storage.getUserById(otherId)));
        return commonFriends;
    }
}
