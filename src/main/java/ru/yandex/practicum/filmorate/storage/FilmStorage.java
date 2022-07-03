package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import java.util.Collection;

public interface FilmStorage {
    Collection<Film> getAll();

    Film addFilm(Film film);

    void deleteFilm(int id);

    Film modifyFilm(int id, Film film);

    Film getFilmById(int id);
}
