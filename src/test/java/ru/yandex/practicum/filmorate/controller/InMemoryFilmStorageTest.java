package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryFilmStorageTest {
    private final InMemoryFilmStorage storage = new InMemoryFilmStorage();

    @Test
    void addFilmRight() throws ValidationException {
        Film film = new Film();
        film.setName("Название");
        film.setDescription("Описание фильма");
        film.setReleaseDate(LocalDate.of(2022, 1, 10));
        film.setDuration(1000);
        storage.addFilm(film);
        assertEquals("Название", film.getName(), "Название не совпадает");
        assertEquals("Описание фильма", film.getDescription(), "Описание не совпадает");
        assertEquals(1000, film.getDuration(), "Длительность не совпадает");
        assertEquals("2022-01-10", film.getReleaseDate().toString(), "Описание не совпадает");
    }

    @Test
    void doNotAddFilmWithOutName() throws ValidationException {
        Film film = new Film();
        film.setName("");
        film.setDescription("Описание второго фильма без названия");
        film.setReleaseDate(LocalDate.of(2010, 5, 13));
        film.setDuration(100);
        Throwable thrown1 = assertThrows(ValidationException.class, () -> {
            storage.addFilm(film);
        });
        assertNotNull(thrown1.getMessage(), "Невозможно запостить фильм без названия");
    }

    @Test
    void doNotAddFilmWithNegativeDuration() throws ValidationException {
        Film film = new Film();
        film.setName("Вот так");
        film.setDescription("Описание фильма с отрицательной длительностью");
        film.setReleaseDate(LocalDate.of(2010, 5, 13));
        film.setDuration(-100);
        Throwable thrown2 = assertThrows(ValidationException.class, () -> {
            storage.addFilm(film);
        });
        assertNotNull(thrown2.getMessage(), "Невозможно запостить фильм с отрицательной длительностью");
    }

    @Test
    void doNotAddSoOldFilm() throws ValidationException {
        Film film = new Film();
        film.setName("Привет");
        film.setDescription("Описание фильма из прошлого");
        film.setReleaseDate(LocalDate.of(1050, 5, 13));
        film.setDuration(100);
        Throwable thrown4 = assertThrows(ValidationException.class, () -> {
            storage.addFilm(film);
        });
        assertNotNull(thrown4.getMessage(), "Невозможно запостить такой старый фильм");
    }

    @Test
    void refreshFilmsRight() throws ValidationException {
        Film film = new Film();
        film.setName("Название");
        film.setDescription("Описание фильма");
        film.setReleaseDate(LocalDate.of(2022, 1, 10));
        film.setDuration(1000);
        storage.addFilm(film);

        Film newFilm = new Film();
        newFilm.setId(film.getId());
        newFilm.setName("Название 2");
        newFilm.setDescription("Описание фильма 2");
        newFilm.setReleaseDate(LocalDate.of(2020, 1, 10));
        newFilm.setDuration(100);
        storage.modifyFilm(newFilm);
        assertEquals(film.getId(), newFilm.getId(), "После обновления id должен остаться прежним");
        assertFalse(storage.getAll().contains(film));
        assertTrue(storage.getAll().contains(newFilm));

        assertEquals(1, storage.getAll().size(), "В списке доложно быть только 1 фильм");
    }

    @Test
    void refreshFilmWithNewIdRight() throws ValidationException {
        Film film = new Film();
        film.setName("Название");
        film.setDescription("Описание фильма");
        film.setReleaseDate(LocalDate.of(2022, 1, 10));
        film.setDuration(1000);
        storage.addFilm(film);

        Film newFilm = new Film();
        int randomNewId = 37;
        newFilm.setId(randomNewId);
        newFilm.setName("Название 3");
        newFilm.setDescription("Описание фильма 3");
        newFilm.setReleaseDate(LocalDate.of(2000, 12, 11));
        newFilm.setDuration(120);
        storage.modifyFilm(newFilm);
        assertTrue(storage.getAll().contains(newFilm));
        assertEquals(2, storage.getAll().size(), "В списке доложно быть 2 фильма");
    }

    @Test
    void returnRightSize() throws ValidationException {
        Film film = new Film();
        film.setName("Название 1");
        film.setDescription("Описание фильма 1");
        film.setReleaseDate(LocalDate.of(1950, 1, 10));
        film.setDuration(10);
        storage.addFilm(film);

        Film newFilm = new Film();
        newFilm.setName("Название 2");
        newFilm.setDescription("Описание фильма 2");
        newFilm.setReleaseDate(LocalDate.of(1990, 6, 20));
        newFilm.setDuration(100);
        storage.addFilm(newFilm);

        Film oneMoreFilm = new Film();
        oneMoreFilm.setName("Название 3");
        oneMoreFilm.setDescription("Описание фильма 3");
        oneMoreFilm.setReleaseDate(LocalDate.of(2020, 12, 30));
        oneMoreFilm.setDuration(1000);
        storage.addFilm(oneMoreFilm);

        assertEquals(3, storage.getAll().size(), "Список должен состоять из 3 фильмов");
    }

    @Test
    void removeFilms() throws ValidationException {
        Film film = new Film();
        film.setName("Название 1");
        film.setDescription("Описание фильма 1");
        film.setReleaseDate(LocalDate.of(1950, 1, 10));
        film.setDuration(10);
        storage.addFilm(film);

        Film newFilm = new Film();
        newFilm.setName("Название 2");
        newFilm.setDescription("Описание фильма 2");
        newFilm.setReleaseDate(LocalDate.of(1990, 6, 20));
        newFilm.setDuration(100);
        storage.addFilm(newFilm);

        Film oneMoreFilm = new Film();
        oneMoreFilm.setName("Название 3");
        oneMoreFilm.setDescription("Описание фильма 3");
        oneMoreFilm.setReleaseDate(LocalDate.of(2020, 12, 30));
        oneMoreFilm.setDuration(1000);
        storage.addFilm(oneMoreFilm);

        storage.deleteFilm(film);
        assertEquals(2, storage.getAll().size(), "Список должен состоять из 2 фильмов");
        storage.deleteFilm(newFilm);
        assertEquals(1, storage.getAll().size(), "В списке должен остаться 1 фильм");
        storage.deleteFilm(oneMoreFilm);
        assertEquals(0, storage.getAll().size(), "Список фильмов должен стать пустым");
    }
}