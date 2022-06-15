package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService service) {
        this.userService = service;
    }

    @GetMapping
    public Collection<User> findAll() {
        return userService.getAll();
    }

    @PostMapping
    public User create(@RequestBody User user) {
        return userService.addUser(user);
    }

    @PutMapping
    public User refresh(@RequestBody User user) {
        return userService.modifyUser(user);
    }

    @DeleteMapping("/{id}")
    public User removeUser(@PathVariable int id) {
        return userService.deleteUser(id);
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable int id) {
        return userService.getUserById(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void setFriendship(@PathVariable int id, @PathVariable int friendId) {
        userService.setFriendship(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void breakFriendship(@PathVariable int id, @PathVariable int friendId) {
        userService.breakFriendship(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public Collection<User> getAllFriends(@PathVariable int id) {
        return userService.getAllFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> getCommonFriendsIds(@PathVariable int id, @PathVariable int otherId) {
        return userService.getCommonFriends(id, otherId);
    }
}
