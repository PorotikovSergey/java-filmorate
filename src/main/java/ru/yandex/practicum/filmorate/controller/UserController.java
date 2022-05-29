package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.IdGenerator;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;
import java.util.regex.Pattern;

@Slf4j
@RestController
public class UserController {
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*\\.\\w{2,4}");
    private final Map<Integer, User> users = new HashMap<>();

    @GetMapping("/users")
    public Collection<User> findAll() {
        log.debug("Текущее количество пользователей: {}", users.size());
        return users.values();
    }

    @PostMapping("/users")
    public User create(@RequestBody User user) throws ValidationException {
        user.setId(IdGenerator.generateUserId());
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.debug("Смена имени юзера: {}", user.getName());
        }
        if (userCheck(user)) {
            users.put(user.getId(), user);
            log.debug("Добавлен юзер: {}", user);
            return user;
        } else {
            throw new ValidationException("Невозможно добавить пользователя " + user.getName() +
                    " с логином " + user.getLogin());
        }
    }

    @PutMapping("/users")
    public User refresh(@RequestBody User user) throws ValidationException {
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.debug("Смена имени юзера: {}", user.getName());
        }
        if (userCheck(user) && !users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            log.debug("Добавлен юзер: {}", user);
            log.debug("Размер мапы с юзерами: {}", users.size());
        } else if (userCheck(user) && users.containsKey(user.getId())) {
            users.replace(user.getId(), user);
            log.debug("Обновлен юзер: {}", user);
            log.debug("Размер мапы с юзерами: {}", users.size());
        } else {
            throw new ValidationException("Невозможно обновить пользователя " + user.getName() +
                    " с логином " + user.getLogin());
        }
        return user;
    }

    //---------------------Проверка юзера на соответствие-------------------------------------
    public boolean userCheck(User user) {
        boolean validId = user.getId() >= 0;
        boolean validEmail = EMAIL_PATTERN.matcher(user.getEmail()).matches();
        boolean validLogin = (!(user.getLogin().isBlank()) && !user.getLogin().contains(" "));
        boolean validBirthday = user.getBirthday().isBefore(LocalDate.now());
        return (validBirthday && validLogin && validEmail && validId);
    }
}
