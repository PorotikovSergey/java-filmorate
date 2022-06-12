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
    public Film create(@RequestBody Film film) {
        return filmStorage.addFilm(film);
    }

    @PutMapping("/films")
    public Film refresh(@RequestBody Film film) {
        return filmStorage.modifyFilm(film);
    }

    @DeleteMapping("/films")
    public Film remove(@RequestBody Film film) {
        return filmStorage.deleteFilm(film);
    }

    @GetMapping("/films/{id}")
    public Film getFilmById(@PathVariable int id) {
        return filmStorage.getFilmById(id);
    }

    @PutMapping("/films/{id}/like/{userId}")
    public void putLikeToFilm(@PathVariable int id, @PathVariable int userId) {
        service.putLike(userId, id);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public void deleteLikeFromFilm(@PathVariable int id, @PathVariable int userId) {
        service.deleteLike(userId, id);
    }

    @GetMapping("/films/popular")
    public List<Film> getCertainAmountOfLikedFilms(@RequestParam Optional<Integer> count) {
        if (count.isPresent()) {
            System.out.println("---------------запрос с count = " + count.get() + "--------------------");
            return service.getCertainAmountOfLikedFilms(count.get());
        } else {
            System.out.println("---------------запрос без count--------------------");
            return service.getCertainAmountOfLikedFilms(10);
        }
    }
}
