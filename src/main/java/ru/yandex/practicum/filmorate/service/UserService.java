package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

@Slf4j
@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void setFriendship(int firstId, int secondId) {
        if (firstId < 0) {
            log.debug("Отрицательный id {} юзера", firstId);
            throw new NotFoundException("Отрицательного id юзера не может быть");
        }
        if (secondId < 0) {
            log.debug("Отрицательный id {} второго юзера", secondId);
            throw new NotFoundException("Отрицательного id юзера не может быть");
        }
        userStorage.getUserById(firstId).getFriends().add(secondId);
        log.debug("У первого юзера стало {} друзей", userStorage.getUserById(firstId).getFriends().size());
        userStorage.getUserById(secondId).getFriends().add(firstId);
        log.debug("У второго юзера стало {} друзей", userStorage.getUserById(secondId).getFriends().size());
    }

    public void breakFriendship(int firstId, int secondId) {
        if ((firstId < 0) || (secondId < 0)) {
            log.debug("Отрицательный id");
            throw new NotFoundException("Отрицательного id не может быть");
        }
        userStorage.getUserById(firstId).getFriends().remove(secondId);
        log.debug("У первого юзера стало {} друзей", userStorage.getUserById(firstId).getFriends().size());
        userStorage.getUserById(secondId).getFriends().remove(firstId);
        log.debug("У второго юзера стало {} друзей", userStorage.getUserById(secondId).getFriends().size());
    }

    public List<User> getAllFriends(int id) {
        List<User> resultList = new ArrayList<>();
        if (id < 0) {
            log.debug("Отрицательный id");
            throw new NotFoundException("Отрицательного id не может быть");
        }
        for (Integer friendId : userStorage.getUserById(id).getFriends()) {
            resultList.add(userStorage.getUserById(friendId));
        }
        log.debug("Друзей по запросу выдано {} ", resultList.size());
        return resultList;
    }

    public List<User> getCommonFriends(int id, int otherId) {
        List<User> resultList = new ArrayList<>();
        if ((userStorage.getUserById(id).getFriends() == null) || (userStorage.getUserById(otherId).getFriends() == null)) {
            return resultList;
        }
        List<Integer> commonFriends = new ArrayList<>(userStorage.getUserById(id).getFriends());
        commonFriends.retainAll(userStorage.getUserById(otherId).getFriends());
        for (Integer friendId : commonFriends) {
            resultList.add(userStorage.getUserById(friendId));
        }
        log.debug("Общих друзей по запросу выдано {} ", resultList.size());
        return resultList;
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

    public User deleteUser(int id) {
        return userStorage.deleteUser(id);
    }

    public User getUserById(int id) {
        return userStorage.getUserById(id);
    }
}
