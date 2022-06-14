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

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService service) {
        this.filmService = service;
    }

    @GetMapping("/films")
    public Collection<Film> findAll() {
        return filmService.getAll();
    }

    @PostMapping("/films")
    public Film create(@RequestBody Film film) {
        return filmService.addFilm(film);
    }

    @PutMapping("/films")
    public Film refresh(@RequestBody Film film) {
        return filmService.modifyFilm(film);
    }

    @DeleteMapping("/films")
    public Film remove(@RequestBody Film film) {
        return filmService.deleteFilm(film);
    }

    @GetMapping("/films/{id}")
    public Film getFilmById(@PathVariable int id) {
        return filmService.getFilmById(id);
    }

    @PutMapping("/films/{id}/like/{userId}")
    public void putLikeToFilm(@PathVariable int id, @PathVariable int userId) {
        filmService.putLike(userId, id);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public void deleteLikeFromFilm(@PathVariable int id, @PathVariable int userId) {
        filmService.deleteLike(userId, id);
    }

    @GetMapping("/films/popular")
    public List<Film> getCertainAmountOfLikedFilms(@RequestParam Optional<Integer> count) {
        if (count.isPresent()) {
            return filmService.getCertainAmountOfLikedFilms(count.get());
        } else {
            return filmService.getCertainAmountOfLikedFilms(10);
        }
    }
}
