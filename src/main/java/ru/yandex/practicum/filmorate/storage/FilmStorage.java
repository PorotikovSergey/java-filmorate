package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import java.util.Collection;

public interface FilmStorage {
    Collection<Film> getAll();

    Film addFilm(Film film);

    Film deleteFilm(int id);

    Film modifyFilm(Film film);

    Film getFilmById(int id);
}
