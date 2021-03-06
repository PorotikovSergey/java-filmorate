package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.text.ParseException;
import java.util.Collection;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService service) {
        this.filmService = service;
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable int id) {
        return filmService.getFilmById(id);
    }

    @DeleteMapping("/{id}")
    public void remove(@PathVariable int id) {
        filmService.deleteFilm(id);
    }

    @GetMapping
    public Collection<Film> findAll() {
        return filmService.getAll();
    }

    @PostMapping
    public Film create(@RequestBody Film film) throws ParseException {
        return filmService.addFilm(film);
    }

    @PutMapping
    public Film refresh(@RequestBody Film film) throws ParseException {
        return filmService.modifyFilm(film);
    }

    @PutMapping("/{filmId}/like/{userId}")
    public void putLikeToFilm(@PathVariable int filmId, @PathVariable int userId) {
        filmService.putLike(filmId, userId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public void deleteLikeFromFilm(@PathVariable int filmId, @PathVariable int userId) {
        filmService.deleteLike(filmId, userId);
    }

    @GetMapping("/popular")
    public Collection<Film> getCertainAmountOfLikedFilms(@RequestParam Optional<Integer> count) {
        if (count.isPresent()) {
            return filmService.getPopular(count.get());
        } else {
            return filmService.getAllPopular();
        }
    }
}
