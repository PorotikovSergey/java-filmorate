package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.*;

//Создайте FilmService, который будет отвечать за операции с фильмами, —
// добавление и удаление лайка, вывод 10 наиболее популярных фильмов по количеству лайков.
// Пусть пока каждый пользователь может поставить лайк фильму только один раз.
@Slf4j
@Service
public class FilmService {
    private final InMemoryFilmStorage filmStorage;
    private final InMemoryUserStorage userStorage;

    @Autowired
    public FilmService(InMemoryFilmStorage storage, InMemoryUserStorage userStorage) {
        this.filmStorage = storage;
        this.userStorage = userStorage;
    }

    public void putLike(int userId, Film film) {
        film.getLikes().add(userId);
        userStorage.getUserById(userId).getLikedFilms().add(film.getId());
    }

    public void deleteLike(int userId, Film film) {
        film.getLikes().remove(userId);
        userStorage.getUserById(userId).getLikedFilms().remove(film.getId());
    }

    public List<Film> getTenBestFilms() {
        List<Film> bestTenFilms = new ArrayList<>(filmStorage.getAll());
        bestTenFilms.sort(new LikesComparator());
        return bestTenFilms;
    }

    public List<Film> getCertainAmountOfLikedFilms(int amount) {
        List<Film> resultListOfBestFilms = new ArrayList<>();
        List<Film> bestFilms = new ArrayList<>(filmStorage.getAll());
        bestFilms.sort(new LikesComparator());
        for (int i = 0; i<amount; i++) {
            resultListOfBestFilms.add(bestFilms.get(i));
        }
        return resultListOfBestFilms;
    }

//-----------------------Компаратор по лайкам-------------------------------
    public static class LikesComparator implements Comparator<Film> {
        @Override
        public int compare(Film film1, Film film2) {
            return film1.getLikes().size()-film2.getLikes().size();
        }
    }
}
