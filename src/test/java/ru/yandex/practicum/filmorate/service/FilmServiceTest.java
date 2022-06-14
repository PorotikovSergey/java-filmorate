package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmServiceTest {
    private final UserStorage userStorage = new InMemoryUserStorage();
    private final FilmStorage filmStorage = new InMemoryFilmStorage();
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
        filmService.addFilm(film);

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
        filmService.addFilm(film);

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
        filmService.addFilm(film);

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
        filmService.addFilm(film2);

        filmService.putLike(1, 1);
        filmService.putLike(2, 1);

        assertEquals(1, filmService.getCertainAmountOfLikedFilms(2).get(0).getId(),
                "Первым в списке должен быть фильм с id 1");
        assertEquals(2, filmService.getCertainAmountOfLikedFilms(2).get(1).getId(),
                "Вторым в списке должен быть фильм с id 2");
    }

    @Test
    void modifyFilm() {
        Film film = new Film();
        film.setName("Название");
        film.setDescription("Описание фильма");
        film.setReleaseDate(LocalDate.of(2022, 1, 10));
        film.setDuration(1000);
        filmService.addFilm(film);

        Film film2 = new Film();
        film2.setName("Название2");
        film2.setDescription("Описание фильма");
        film2.setReleaseDate(LocalDate.of(2022, 1, 10));
        film2.setDuration(1000);
        filmService.addFilm(film2);

        assertEquals("Название", filmService.getFilmById(1).getName(),
                "Название первого фильма не совпадает");

        Film film3 = new Film();
        film3.setName("Название3");
        film3.setDescription("Описание фильма");
        film3.setReleaseDate(LocalDate.of(2022, 1, 10));
        film3.setDuration(1000);
        film3.setId(1);

        filmService.modifyFilm(film3);
        assertEquals("Название3", filmService.getFilmById(1).getName(),
                "Название первого фильма должно поменяться на 'Название3'");
    }

    @Test
    void deleteFilms() {
        Film film = new Film();
        film.setName("Название");
        film.setDescription("Описание фильма");
        film.setReleaseDate(LocalDate.of(2022, 1, 10));
        film.setDuration(1000);
        filmService.addFilm(film);

        Film film2 = new Film();
        film2.setName("Название2");
        film2.setDescription("Описание фильма");
        film2.setReleaseDate(LocalDate.of(2022, 1, 10));
        film2.setDuration(1000);
        filmService.addFilm(film2);

        Film film3 = new Film();
        film3.setName("Название3");
        film3.setDescription("Описание фильма");
        film3.setReleaseDate(LocalDate.of(2022, 1, 10));
        film3.setDuration(1000);
        filmService.addFilm(film3);

        assertEquals(3, filmService.getAll().size(), "Должно быть 3 фильма");
        filmService.deleteFilm(1);
        assertEquals(2, filmService.getAll().size(), "Должно остаться 2 фильма");
        filmService.deleteFilm(2);
        filmService.deleteFilm(3);

        assertTrue(filmService.getAll().isEmpty(), "Список фильмов должен стать пустым");
    }
}