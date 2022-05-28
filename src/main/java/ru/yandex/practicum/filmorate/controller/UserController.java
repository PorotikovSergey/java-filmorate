package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.controller.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;

@RestController
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final Map<Integer, User> users = new HashMap<>();

    @GetMapping("/users")
    public Collection<User> findAll() {
        log.debug("Текущее количество пользователей: {}", users.size());
        return users.values();
    }

    @PostMapping(value = "/users")
    public User create(@RequestBody User user) throws ValidationException {
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.debug("Смена имени юзера: {}", user.getName());
        }
        if(users.containsKey(user.getId())) {
            user.setId(user.getId()+1);
        }
        if (userCheck(user) && !users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            log.debug("Добавлен юзер: {}", user);
            return user;
        } else {
            throw new ValidationException("Невозможно добавить пользователя " + user.getName() +
                    " с логином " + user.getLogin());
        }
    }

    @PutMapping(value = "/users")
    public User refresh(@RequestBody User user) throws ValidationException {
        if (userCheck(user) && !users.containsKey(user.getId())) {
            if (user.getName().isEmpty()) {
                user.setName(user.getLogin());
                log.debug("Смена имени юзера: {}", user.getName());
            }
            users.put(user.getId(), user);
            log.debug("Добавлен юзер: {}", user);
            return user;
        }
        if (userCheck(user) && users.containsKey(user.getId())) {
            if (user.getName().isBlank()) {
                user.setName(user.getLogin());
                log.debug("Смена имени юзера: {}", user.getName());
            }
            users.replace(user.getId(), user);
            log.debug("Добавлен(обновлен) юзер: {}", user);
            return user;
        }
        throw new ValidationException("Невозможно обновить пользователя " + user.getName() +
                " с логином " + user.getLogin());
    }

    //---------------------Проверка юзера на соответствие-------------------------------------
    public static boolean userCheck(User user) {
        boolean validId = user.getId() >= 0;
        boolean validEmail = (!user.getEmail().isBlank() && user.getEmail().contains("@"));
        boolean validLogin = (!(user.getLogin().isBlank()) && !user.getLogin().contains(" "));
        boolean validBirthday = user.getBirthday().isBefore(LocalDate.now());
        return (validBirthday && validLogin && validEmail && validId);
    }
}
