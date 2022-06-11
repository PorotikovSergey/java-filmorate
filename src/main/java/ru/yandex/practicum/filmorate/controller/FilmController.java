package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
public class FilmController {
    private final InMemoryFilmStorage filmStorage;
    private final FilmService service;

    @Autowired
    public FilmController(InMemoryFilmStorage storage, FilmService service) {
        this.filmStorage = storage;
        this.service = service;
    }

    @GetMapping("/films")
    public Collection<Film> findAll() {
        return filmStorage.getAll();
    }

    @PostMapping("/films")
    public Film create(@RequestBody Film film) throws ValidationException {
        return filmStorage.addFilm(film);
    }

    @PutMapping("/films")
    public Film refresh(@RequestBody Film film) throws ValidationException {
        return filmStorage.modifyFilm(film);
    }

    @DeleteMapping("/films")
    public Film remove(@RequestBody Film film) {
        return filmStorage.deleteFilm(film);
    }


    @PutMapping("/films/{id}/like/{userId}")
    public void putLikeToFilm(@PathVariable int id, @PathVariable int userId) {
        service.putLike(userId, filmStorage.getFilmDyId(id));
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public void deleteLikeFromFilm(@PathVariable int id, @PathVariable int userId) {
        service.deleteLike(userId, filmStorage.getFilmDyId(id));
    }

    @GetMapping("/films/popular?count={count}")
    public List<Film> getCertainAmountOfLikedFilms(@PathVariable Optional<Integer> count) {
        if (count.isPresent()) {
            return service.getCertainAmountOfLikedFilms(count.get());
        } else {
            return service.getCertainAmountOfLikedFilms(10);
        }
    }
}
