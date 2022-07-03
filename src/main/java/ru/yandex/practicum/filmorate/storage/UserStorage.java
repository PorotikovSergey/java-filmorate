package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import java.util.Collection;

public interface UserStorage {
    Collection<User> getAll();

    User addUser(User user);

    User modifyUser(int id, User user);

    void deleteUser(int id);

    User getUserById(int id);

    Collection<User> getAllConfirmedFriends(int id);

    Collection<User> getAllUnconfirmedFriends(int id);

    Collection<Film> getAllLikedFilms(int id);
}
