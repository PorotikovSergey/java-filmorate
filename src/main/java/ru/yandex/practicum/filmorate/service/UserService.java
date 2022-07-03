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

//    public void setFriendship(int firstId, int secondId) {
//    }
//
//    public void breakFriendship(int firstId, int secondId) {
//    }
//
//    public List<User> getAllFriends(int id) {
//    }
//
//    public List<User> getCommonFriends(int id, int otherId) {
//    }
//
//    public Collection<User> getAll() {
//    }
//
//    public User addUser(User user) {
//    }
//
//    public User modifyUser(User user) {
//    }
//
//    public User deleteUser(int id) {
//    }
}
