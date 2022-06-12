package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.*;

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

    public void putLike(int userId, int filmId) {
        if ((userId < 0) || (filmId < 0)) {
            throw new NotFoundException("Отрицательного id не может быть");
        }
        if (userStorage.getUserById(userId) == null) {
            throw new NotFoundException("Юзера с такими id не существует");
        }
        if (filmStorage.getFilmById(filmId) == null) {
            throw new NotFoundException("Фильма с такими id не существует");
        }
        filmStorage.getFilmById(filmId).getLikes().add(userId);
        userStorage.getUserById(userId).getLikedFilms().add(filmId);
    }

    public void deleteLike(int userId, int filmId) {
        if ((userId < 0) || (filmId < 0)) {
            throw new NotFoundException("Отрицательного id не может быть");
        }
        if (userStorage.getUserById(userId) == null) {
            throw new NotFoundException("Юзера с такими id не существует");
        }
        if (filmStorage.getFilmById(filmId) == null) {
            throw new NotFoundException("Фильма с такими id не существует");
        }
        filmStorage.getFilmById(filmId).getLikes().remove(userId);
        userStorage.getUserById(userId).getLikedFilms().remove(filmId);
    }

    public List<Film> getCertainAmountOfLikedFilms(int amount) {
        if (amount < 0) {
            throw new NotFoundException("Отрицательного количества не может быть");
        }
        List<Film> resultListOfBestFilms = new ArrayList<>();
        List<Film> bestFilms = new ArrayList<>(filmStorage.getAll());
        if (amount > bestFilms.size()) {
            amount = bestFilms.size();
        }
        bestFilms.sort(new LikesComparator());
        for (int i = 0; i < amount; i++) {
            resultListOfBestFilms.add(bestFilms.get(i));
        }
        return resultListOfBestFilms;
    }

    public static class LikesComparator implements Comparator<Film> {
        @Override
        public int compare(Film film1, Film film2) {
            return film2.getLikes().size() - film1.getLikes().size();
        }
    }
}
