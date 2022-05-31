package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.IdGenerator;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
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
        validate(user);
        user.setId(IdGenerator.generateUserId());
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.debug("Смена имени юзера: {}", user.getName());
        }
        users.put(user.getId(), user);
        log.debug("Добавлен юзер: {}", user);
        return user;
    }

    @PutMapping("/users")
    public User refresh(@RequestBody User user) throws ValidationException {
        validate(user);
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.debug("Смена имени юзера: {}", user.getName());
        }
        if (!users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            log.debug("Добавлен юзер: {}", user);
            log.debug("Размер мапы с юзерами: {}", users.size());
        } else {
            users.replace(user.getId(), user);
            log.debug("Обновлен юзер: {}", user);
            log.debug("Размер мапы с юзерами: {}", users.size());
        }
        return user;
    }

    //---------------------Проверка юзера на соответствие-------------------------------------
    private void validate(User user) throws ValidationException {
        if (user.getId() < 0) {
            throw new ValidationException("Id юзера не может быть отрицательным. " +
                    "Вы пытаетесь задать id: " + user.getId());
        }
        if (!EMAIL_PATTERN.matcher(user.getEmail()).matches()) {
            throw new ValidationException("Email " + user.getEmail() + " не соответсвтует требованиям.");
        }
        if ((user.getLogin().isBlank()) || user.getLogin().contains(" ")) {
            throw new ValidationException("Логин " + user.getLogin() + " не соответствет требованиям.");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Указанная дата рождения " + user.getBirthday() + " находится в будущем.");
        }
    }
}
