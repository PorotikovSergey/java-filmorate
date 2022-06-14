package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;

@Slf4j
@RestController
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService service) {
        this.userService = service;
    }

    @GetMapping("/users")
    public Collection<User> findAll() {
        return userService.getAll();
    }

    @PostMapping("/users")
    public User create(@RequestBody User user) {
        return userService.addUser(user);
    }

    @PutMapping("/users")
    public User refresh(@RequestBody User user) {
        return userService.modifyUser(user);
    }

    @DeleteMapping("/users")
    public User removeUser(@RequestBody User user) {
        return userService.deleteUser(user);
    }

    @GetMapping("/users/{id}")
    public User getUserById(@PathVariable int id) {
        return userService.getUserById(id);
    }

    //--------------------------Переменная пути--------------------------------------------
    @PutMapping("/users/{id}/friends/{friendId}")
    public void setFriendship(@PathVariable int id, @PathVariable int friendId) {
        userService.setFriendship(id, friendId);
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public void breakFriendship(@PathVariable int id, @PathVariable int friendId) {
        userService.breakFriendship(id, friendId);
    }

    @GetMapping("/users/{id}/friends")
    public Collection<User> getAllFriends(@PathVariable int id) {
        return userService.getAllFriends(id);
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public Collection<User> getCommonFriendsIds(@PathVariable int id, @PathVariable int otherId) {
        return userService.getCommonFriends(id, otherId);
    }
}
