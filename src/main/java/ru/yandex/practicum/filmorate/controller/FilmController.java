package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.util.Collection;

@Slf4j
@RestController
public class FilmController {
    private final InMemoryFilmStorage storage;

    @Autowired
    public FilmController(InMemoryFilmStorage storage) {
        this.storage = storage;
    }

    @GetMapping("/films")
    public Collection<Film> findAll() {
        return storage.getAll();
    }

    @PostMapping("/films")
    public Film create(@RequestBody Film film) throws ValidationException {
        return storage.addFilm(film);
    }

    @PutMapping("/films")
    public Film refresh(@RequestBody Film film) throws ValidationException {
        return storage.modifyFilm(film);
    }

    @DeleteMapping("/films")
    public Film remove(@RequestBody Film film) {
        return storage.deleteFilm(film);
    }
}
