package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
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
public class InMemoryUserStorage implements UserStorage{
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*\\.\\w{2,4}");
    private final Map<Integer, User> users = new HashMap<>();


    @Override
    public Collection<User> getAll() {
        log.debug("Текущее количество пользователей: {}", users.size());
        return users.values();
    }

    @Override
    public User addUser(User user) throws ValidationException {
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
    public User deleteUser(User user) {
        if (users.containsKey(user.getId())) {
            users.remove(user.getId());
            return user;
        } else {
            log.debug("Фильма с Id {} не существует", user.getId());
            return null;         //Вот тут надо как-то по-другому! НЕ ЗАБЫТЬ ИСПРАВИТЬ!!!
        }
    }

    @Override
    public User modifyUser(User user) throws ValidationException {
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

//----------------------------Валидация---------------------------------------------------------------
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
