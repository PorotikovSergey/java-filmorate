package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.text.ParseException;
import java.util.Collection;

public interface FilmStorage {
    Collection<Film> getAll();

    Film addFilm(Film film) throws ParseException;

    void deleteFilm(int id);

    Film getFilmById(int id);

    Film modifyFilm(Film film) throws ParseException;

    void putLike(int filmId, int userId);

    void deleteLike(int filmId, int userId);

    Collection<Film> getPopular(int count);

    Collection<Film> getAllPopular();
}
