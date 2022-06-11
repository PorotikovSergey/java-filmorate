package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.util.*;

//Создайте FilmService, который будет отвечать за операции с фильмами, —
// добавление и удаление лайка, вывод 10 наиболее популярных фильмов по количеству лайков.
// Пусть пока каждый пользователь может поставить лайк фильму только один раз.
@Slf4j
@Service
public class FilmService {
    private InMemoryFilmStorage storage;

    @Autowired
    public FilmService(InMemoryFilmStorage storage) {
        this.storage = storage;
    }

    public void putLike(User user, Film film) {
        film.getLikes().add(user.getId());
        user.getLikedFilms().add(film.getId());
    }

    public List<Film> getTenBestFilms() {
        List<Film> bestTenFilms = new ArrayList<>(storage.getAll());
        bestTenFilms.sort(new LikesComparator());
        return bestTenFilms;
    }

//-----------------------Компаратор по лайкам-------------------------------
    public class LikesComparator implements Comparator<Film> {
        @Override
        public int compare(Film film1, Film film2) {
            return film1.getLikes().size()-film2.getLikes().size();
        }
    }
}
