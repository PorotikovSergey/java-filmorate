package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

@Slf4j
@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User getUserById(int id) {
        return userStorage.getUserById(id);
    }

    public Collection<User> getAll() {
        return userStorage.getAll();
    }

    public User addUser(User user) {
        return userStorage.addUser(user);
    }

    public User modifyUser(User user) {
        return userStorage.modifyUser(user);
    }

    public List<User> getAllFriends(int id) {
        return userStorage.getAllConfirmedFriends(id);
    }

    public void setFriendship(int firstId, int secondId) {
        userStorage.setFriendship(firstId, secondId);
    }

    public void breakFriendship(int firstId, int secondId) {
        userStorage.breakFriendship(firstId, secondId);
    }

    public void deleteUser(int id) {
        userStorage.deleteUser(id);
    }

    public Collection<User> getCommonFriends(int id, int otherId) {
        return userStorage.getCommonFriends(id, otherId);
    }
}
