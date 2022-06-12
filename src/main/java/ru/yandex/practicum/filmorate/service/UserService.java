package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.*;

//Создайте UserService, который будет отвечать за такие операции с пользователями,
// как добавление в друзья, удаление из друзей, вывод списка общих друзей.
// Пока пользователям не надо одобрять заявки в друзья — добавляем сразу.
// То есть если Лена стала другом Саши, то это значит, что Саша теперь друг Лены.
@Slf4j
@Service
public class UserService {
    private final InMemoryFilmStorage filmStorage;
    private final InMemoryUserStorage userStorage;

    @Autowired
    public UserService(InMemoryFilmStorage filmStorage, InMemoryUserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public void setFriendship(int firstId, int secondId) {
        if ((firstId < 0) || (secondId < 0)) {
            throw new NotFoundException("Отрицательного id не может быть");
        }
        userStorage.getUserById(firstId).getFriends().add(secondId);
        userStorage.getUserById(secondId).getFriends().add(firstId);
    }

    public void breakFriendship(int firstId, int secondId) {
        if ((firstId < 0) || (secondId < 0)) {
            throw new NotFoundException("Отрицательного id не может быть");
        }
        userStorage.getUserById(firstId).getFriends().remove(secondId);
        userStorage.getUserById(secondId).getFriends().remove(firstId);
    }

    public List<User> getAllFriendsId(int id) {
        List<User> resultList = new ArrayList<>();
        if(id < 0) {
            throw new NotFoundException("Отрицательного id не может быть");
        }
        for(Integer friendId: userStorage.getUserById(id).getFriends()) {
            resultList.add(userStorage.getUserById(friendId));
        }
        return resultList;
    }

    public void putLike(User user, int filmId) {
        filmStorage.getFilmById(filmId).getLikes().add(user.getId());
        user.getLikedFilms().add(filmId);
    }

    public void deleteLike(User user, int filmId) {
        filmStorage.getFilmById(filmId).getLikes().remove(user.getId());
        user.getLikedFilms().remove(filmId);
    }

    public List<User> getCommonFriends(int id, int otherId) {
        List<User> resultList = new ArrayList<>();
        if ((userStorage.getUserById(id).getFriends() == null) || (userStorage.getUserById(otherId).getFriends() == null)) {
            return new ArrayList<>();
        }
        List<Integer> commonFriends = new ArrayList<>(userStorage.getUserById(id).getFriends());
        commonFriends.retainAll(userStorage.getUserById(otherId).getFriends());
        for(Integer friendId: commonFriends) {
            resultList.add(userStorage.getUserById(friendId));
        }
        return resultList;
    }
}
