package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage, @Qualifier("userDbStorage") UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film getFilmById(int id) {
        return filmStorage.getFilmById(id);
    }
//
//    public void putLike(int userId, int filmId) {
//    }
//
//    public void deleteLike(int userId, int filmId) {
//    }
//
//    public Collection<Film> getAll() {
//    }
//
//    public Film addFilm(Film film) {
//    }
//
//    public Film modifyFilm(Film film) {
//    }
//
//    public Film deleteFilm(int id) {
//    }
}
