package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {
    private FilmController controller = new FilmController();

    @Test
    void addFilmsRight() throws ValidationException {
        Film film1 = new Film();
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
        film2.setName("");
        film2.setDescription("Описание второго фильма без названия");
        film2.setReleaseDate(LocalDate.of(2010, 5, 13));
        film2.setDuration(100);
        Throwable thrown1 = assertThrows(ValidationException.class, () -> {
            controller.create(film2);
        });
        assertNotNull(thrown1.getMessage(), "Невозможно запостить фильм без названия");

        Film film3 = new Film();
        film3.setName("Вот так");
        film3.setDescription("Описание фильма с отрицательной длительностью");
        film3.setReleaseDate(LocalDate.of(2010, 5, 13));
        film3.setDuration(-100);
        Throwable thrown2 = assertThrows(ValidationException.class, () -> {
            controller.create(film3);
        });
        assertNotNull(thrown2.getMessage(), "Невозможно запостить фильм с отрицательной длительностью");

        Film film4 = new Film();
        film4.setName("Привет");
        film4.setDescription("Описание фильма из прошлого");
        film4.setReleaseDate(LocalDate.of(1050, 5, 13));
        film4.setDuration(100);
        Throwable thrown4 = assertThrows(ValidationException.class, () -> {
            controller.create(film4);
        });
        assertNotNull(thrown1.getMessage(), "Невозможно запостить такой старый фильм");

        assertEquals(1, controller.findAll().size(), "В список в итоге должен был добавиться "
                + "только один первый фильм");
    }

    @Test
    void refreshFilmsRight() throws ValidationException {
        Film film1 = new Film();
        film1.setName("Название");
        film1.setDescription("Описание фильма");
        film1.setReleaseDate(LocalDate.of(2022, 1, 10));
        film1.setDuration(1000);
        controller.create(film1);
        assertEquals("Название", film1.getName(), "Название не совпадает");

        Film film2 = new Film();
        film2.setId(film1.getId());
        film2.setName("Название 2");
        film2.setDescription("Описание фильма 2");
        film2.setReleaseDate(LocalDate.of(2020, 1, 10));
        film2.setDuration(100);
        controller.refresh(film2);
        assertEquals(film1.getId(), film2.getId(), "После обновления id должен остаться прежним");
        assertFalse(controller.findAll().contains(film1));
        assertTrue(controller.findAll().contains(film2));

        Film film3 = new Film();
        int randomNewId = 37;
        film3.setId(randomNewId);
        film3.setName("Название 3");
        film3.setDescription("Описание фильма 3");
        film3.setReleaseDate(LocalDate.of(2000, 12, 11));
        film3.setDuration(120);
        controller.refresh(film3);
        assertTrue(controller.findAll().contains(film3));
        System.out.println(controller.findAll());
        assertEquals(2, controller.findAll().size(), "В списке доложно быть 2 фильма-" +
                "Один обновлённый и один новый");
    }

    @Test
    void returnRightSize() throws ValidationException {
        Film film1 = new Film();
        film1.setName("Название 1");
        film1.setDescription("Описание фильма 1");
        film1.setReleaseDate(LocalDate.of(1950, 1, 10));
        film1.setDuration(10);
        controller.create(film1);

        Film film2 = new Film();
        film2.setName("Название 2");
        film2.setDescription("Описание фильма 2");
        film2.setReleaseDate(LocalDate.of(1990, 6, 20));
        film2.setDuration(100);
        controller.create(film2);

        Film film3 = new Film();
        film3.setName("Название 3");
        film3.setDescription("Описание фильма 3");
        film3.setReleaseDate(LocalDate.of(2020, 12, 30));
        film3.setDuration(1000);
        controller.create(film3);

        assertEquals(3, controller.findAll().size(), "Список должен состоять из 3 фильмов");
    }

}