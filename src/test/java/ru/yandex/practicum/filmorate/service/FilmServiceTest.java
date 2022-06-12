package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmServiceTest {
    private final InMemoryUserStorage userStorage = new InMemoryUserStorage();
    private final InMemoryFilmStorage filmStorage = new InMemoryFilmStorage();
    private final FilmService filmService = new FilmService(filmStorage, userStorage);

    @BeforeEach
    void clear() {
        IdGenerator.setUserId(0);
        IdGenerator.setFilmId(0);
    }

    @Test
    void putLike() {
        User user = new User();
        user.setName("Вася");
        user.setLogin("Нагибатор3000");
        user.setEmail("nagibator@mail.ru");
        user.setBirthday(LocalDate.of(1999, 6, 26));
        userStorage.addUser(user);

        Film film = new Film();
        film.setName("Название");
        film.setDescription("Описание фильма");
        film.setReleaseDate(LocalDate.of(2022, 1, 10));
        film.setDuration(1000);
        filmStorage.addFilm(film);

        filmService.putLike(1, 1);
        assertEquals(1, film.getLikes().size(), "Должен быть 1 лайк");
        assertEquals(1, user.getLikedFilms().size(), "Должен быть 1 лайкнутый фильм");
    }

    @Test
    void deleteLike() {
        User user = new User();
        user.setName("Вася");
        user.setLogin("Нагибатор3000");
        user.setEmail("nagibator@mail.ru");
        user.setBirthday(LocalDate.of(1999, 6, 26));
        userStorage.addUser(user);

        Film film = new Film();
        film.setName("Название");
        film.setDescription("Описание фильма");
        film.setReleaseDate(LocalDate.of(2022, 1, 10));
        film.setDuration(1000);
        filmStorage.addFilm(film);

        filmService.putLike(1, 1);
        filmService.deleteLike(1, 1);
        assertEquals(0, film.getLikes().size(), "Должно быть 0 лайков");
        assertEquals(0, user.getLikedFilms().size(), "Должно быть 0 лайкнутых фильмов");
    }

    @Test
    void getCertainAmountOfLikedFilms() {
        User user = new User();
        user.setName("Вася");
        user.setLogin("Нагибатор3000");
        user.setEmail("nagibator@mail.ru");
        user.setBirthday(LocalDate.of(1999, 6, 26));
        userStorage.addUser(user);

        Film film = new Film();
        film.setName("Название");
        film.setDescription("Описание фильма");
        film.setReleaseDate(LocalDate.of(2022, 1, 10));
        film.setDuration(1000);
        filmStorage.addFilm(film);

        User user2 = new User();
        user2.setName("Вася2");
        user2.setLogin("Нагибатор3000");
        user2.setEmail("nagibator@mail.ru");
        user2.setBirthday(LocalDate.of(1999, 6, 26));
        userStorage.addUser(user2);

        Film film2 = new Film();
        film2.setName("Название2");
        film2.setDescription("Описание фильма");
        film2.setReleaseDate(LocalDate.of(2022, 1, 10));
        film2.setDuration(1000);
        filmStorage.addFilm(film2);

        filmService.putLike(1, 1);
        filmService.putLike(2, 1);

        assertEquals(1, filmService.getCertainAmountOfLikedFilms(2).get(0).getId(),
                "Первым в списке должен быть фильм с id 1");
        assertEquals(2, filmService.getCertainAmountOfLikedFilms(2).get(1).getId(),
                "Вторым в списке должен быть фильм с id 2");

    }
}