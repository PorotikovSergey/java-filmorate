package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.IdGenerator;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
public class FilmController {
    private static final int MAX_DESCRIPTION_LENGTH = 200;
    private static final LocalDate FIRST_CINEMA_DATE = LocalDate.of(1895, 12, 28);
    private final Map<Integer, Film> films = new HashMap<>();

    @GetMapping("/films")
    public Collection<Film> findAll() {
        log.debug("Текущее количество фильмов: {}", films.size());
        return films.values();
    }

    @PostMapping("/films")
    public Film create(@RequestBody Film film) throws ValidationException {
        film.setId(IdGenerator.generateFilmId());
        validate(film);
        films.put(film.getId(), film);
        log.debug("Добавлен фильм: {}", film);
        log.debug("Размер мапы с фильмами: {}", films.size());
        return film;
    }

    @PutMapping("/films")
    public Film refresh(@RequestBody Film film) throws ValidationException {
        validate(film);
        if (!films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            log.debug("Обновлён(добавлен) фильм: {}", film);
            log.debug("Размер мапы с фильмами-{} после обновления(добавления) фильма: {}", films.size(), film);
        } else {
            films.replace(film.getId(), film);
            log.debug("Обновлён фильм: {}", film);
            log.debug("Размер мапы с фильмами-{} после обновления фильма: {}", films.size(), film);
        }
        return film;
    }

    //-------------------Проверка фильма на соотвтетствие-----------------------------------------
    private void validate(Film film) throws ValidationException {
        boolean validId = film.getId() >= 0;
        boolean validName = !film.getName().isBlank();
        boolean validDescription = film.getDescription().length() <= MAX_DESCRIPTION_LENGTH;
        boolean validDate = film.getReleaseDate().isAfter(FIRST_CINEMA_DATE);
        boolean validDuration = film.getDuration() >= 0;
        if (!(validDate && validDuration && validDescription && validId && validName)) {
            throw new ValidationException("Невозможно запостить фильм " + film.getName());
        }
    }
}
