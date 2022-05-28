package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.controller.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;

@RestController
public class FilmController {
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    private final HashMap<Integer, Film> films = new HashMap<>();
    private static final int MAX_DESCRIPTION_LENGTH = 200;
    private static final LocalDate FIRST_CINEMA_DATE = LocalDate.of(1895, 12, 28);


    @GetMapping("/films")
    public Collection<Film> findAll() {
        log.debug("Текущее количество фильмов: {}", films.size());
        return films.values();
    }

    @PostMapping(value = "/films")
    public Film create(@RequestBody Film film) throws ValidationException {
        if (filmCheck(film) && !films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            log.debug("Добавлен фильм: {}", film);
            return film;
        } else {
            throw new ValidationException("Невозможно запостить фильм " + film.getName());
        }
    }

    @PutMapping(value = "/films")
    public Film refresh(@RequestBody Film film) throws ValidationException {
        if (filmCheck(film) && films.containsKey(film.getId())) {
            films.replace(film.getId(), film);
            log.debug("Обновлён(добавлен) фильм: {}", film);
            return film;
        }
        if (filmCheck(film) && films.containsKey(film.getId())) {
            films.replace(film.getId(), film);
            log.debug("Обновлён фильм: {}", film);
            return film;
        }
        throw new ValidationException("Невозможно обновить фильм " + film.getName());
    }

    //-------------------Проверка фильма на соотвтетствие-----------------------------------------
    private boolean filmCheck(Film film) {
        boolean validId = film.getId() >=0;
        boolean validName = !film.getName().isBlank();
        boolean validDescription = film.getDescription().length() <= MAX_DESCRIPTION_LENGTH;
        boolean validDate = film.getReleaseDate().isAfter(FIRST_CINEMA_DATE);
        boolean validDuration = film.getDuration()>=0;

        return (validDate && validDuration && validDescription && validId && validName);
    }
}
