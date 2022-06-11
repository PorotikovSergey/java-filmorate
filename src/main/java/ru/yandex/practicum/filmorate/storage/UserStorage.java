package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {
    Collection<User> getAll();

    User addUser(User user) throws ValidationException;

    User deleteUser(User user);

    User modifyUser(User user) throws ValidationException;
}
