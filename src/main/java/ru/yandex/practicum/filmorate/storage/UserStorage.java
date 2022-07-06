package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;
import java.util.Collection;

public interface UserStorage {
    Collection<User> getAll();

    User addUser(User user);

    void deleteUser(int id);

    User getUserById(int id);

    User modifyUser(User user);
}
