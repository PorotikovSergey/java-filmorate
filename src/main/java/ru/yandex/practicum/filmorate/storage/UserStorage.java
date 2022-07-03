package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;
import java.util.Collection;
import java.util.Optional;

public interface UserStorage {
    Collection<User> getAll();

    User addUser(User user);

    User deleteUser(int id);

    User modifyUser(User user);

    Optional<User> getUserById(int id);
}
