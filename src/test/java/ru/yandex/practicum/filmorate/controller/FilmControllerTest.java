package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {
    private FilmController controller = new FilmController();

    @Test
    void addFilmsRight() throws ValidationException {
        Film film1 = new Film();
        film1.setId(1);
        film1.setName("Название");
        film1.setDescription("Описание фильма");
        film1.setReleaseDate(LocalDate.of(2022, 1, 10));
        film1.setDuration(1000);
        controller.create(film1);
        assertEquals("Название", film1.getName(), "Название не совпадает");
        assertEquals("Описание фильма", film1.getDescription(), "Описание не совпадает");
        assertEquals(1000, film1.getDuration(), "Длительность не совпадает");
        assertEquals("2022-01-10", film1.getReleaseDate().toString(), "Описание не совпадает");

        Film film2 = new Film();
        film2.setId(1);
        film2.setName("Привет");
        film2.setDescription("Описание второго фильма с чужим id");
        film2.setReleaseDate(LocalDate.of(2010, 5, 13));
        film2.setDuration(100);
        Throwable thrown1 = assertThrows(ValidationException.class, () -> {
            controller.create(film2);
        });
        assertNotNull(thrown1.getMessage(), "Невозможно запостить фильм");

        Film film3 = new Film();
        film3.setId(3);
        film3.setName("");
        film3.setDescription("Описание фильма без названия");
        film3.setReleaseDate(LocalDate.of(2010, 5, 13));
        film3.setDuration(100);
        Throwable thrown2 = assertThrows(ValidationException.class, () -> {
            controller.create(film3);
        });
        assertNotNull(thrown2.getMessage(), "Невозможно запостить фильм");

        Film film4 = new Film();
        film4.setId(4);
        film4.setName("Привет");
        film4.setDescription("Описание фильма из прошлого");
        film4.setReleaseDate(LocalDate.of(1050, 5, 13));
        film4.setDuration(100);
        Throwable thrown4 = assertThrows(ValidationException.class, () -> {
            controller.create(film4);
        });
        assertNotNull(thrown1.getMessage(), "Невозможно запостить фильм");

    }

}