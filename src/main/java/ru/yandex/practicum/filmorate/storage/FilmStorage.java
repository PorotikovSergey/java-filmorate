package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface FilmStorage {
    Collection<Film> getAll();

    Film addFilm(Film film);

    void deleteFilm(int id);

    Film getFilmById(int id);

    Film modifyFilm(Film film);

    void putLike(int userId, int filmId);

    void deleteLike(int userId, int filmId);
}
