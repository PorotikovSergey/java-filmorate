package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {
    Collection<Film> getAll();

    Film addFilm(Film film) throws ValidationException;

    Film deleteFilm(Film film);

    Film modifyFilm(Film film) throws ValidationException;
}
