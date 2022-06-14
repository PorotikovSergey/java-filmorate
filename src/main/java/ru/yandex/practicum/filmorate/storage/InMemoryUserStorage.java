package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.IdGenerator;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*\\.\\w{2,4}");

    private final Map<Integer, User> users = new HashMap<>();

    @Override
    public Collection<User> getAll() {
        log.debug("Текущее количество пользователей: {}", users.size());
        return users.values();
    }

    @Override
    public User addUser(User user) {
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

    @Override
    public User deleteUser(int id) {
        if (users.containsKey(id)) {
            return users.remove(id);
        } else {
            log.debug("Юзера с Id {} не существует", id);
            throw new NotFoundException("Юзера с таким ID не существует");
        }
    }

    @Override
    public User modifyUser(User user) {
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

    public User getUserById(int id) {
        if (id < 0) {
            throw new NotFoundException("Id не может быть меньше нуля");
        }
        return users.get(id);
    }

    private void validate(User user) {
        if (user.getId() < 0) {
            throw new NotFoundException("Id не может быть меньше нуля");
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
