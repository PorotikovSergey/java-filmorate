package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.util.Collection;

//Создайте UserService, который будет отвечать за такие операции с пользователями,
// как добавление в друзья, удаление из друзей, вывод списка общих друзей.
// Пока пользователям не надо одобрять заявки в друзья — добавляем сразу.
// То есть если Лена стала другом Саши, то это значит, что Саша теперь друг Лены.
@Slf4j
@Service
public class UserService {
    private final InMemoryFilmStorage filmStorage;

    @Autowired
    public UserService(InMemoryFilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public void setFriendship(User user1, User user2) {
        user1.getFriends().add(user2.getId());
        user2.getFriends().add(user1.getId());
    }

    public void breakFriendship(User user1, User user2) {
        user1.getFriends().remove(user2.getId());
        user2.getFriends().remove(user1.getId());
    }

    public Collection<Integer> getAllFriendsId(User user) {
        return user.getFriends();
    }

    public void putLike(User user, int filmId) {
        filmStorage.getFilmDyId(filmId).getLikes().add(user.getId());
        user.getLikedFilms().add(filmId);
    }

    public void deleteLike(User user, int filmId) {
        filmStorage.getFilmDyId(filmId).getLikes().remove(user.getId());
        user.getLikedFilms().remove(filmId);
    }
}
