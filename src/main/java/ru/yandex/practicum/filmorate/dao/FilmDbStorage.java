package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Collection;


@Component
@Slf4j
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film getFilmById(int id) {
        return jdbcTemplate.query("select * from films where film_id = ?", new Object[]{id}, new FilmMapper())
                .stream().findAny().orElse(null);
    }

    @Override
    public Collection<Film> getAll() {
        return jdbcTemplate.query("SELECT * FROM films", new FilmMapper());
    }

    @Override
    public Film addFilm(Film film) {
        jdbcTemplate.update("INSERT INTO users (FILM_NAME, FILM_DESCRIPTION, " +
                        "FILM_DURATION, FILM_RELEASEDATE, FILM_RATING) " +
                        "VALUES (?,?,?,?,?)", film.getName(), film.getDescription(),
                film.getDuration(), film.getReleaseDate(), film.getRating());
        return film;
    }

    @Override
    public Film modifyFilm(int id, Film film) {
        jdbcTemplate.update("UPDATE films SET film_name=?, film_description=?, " +
                        "film_duration=?, film_releaseDate=?, film_rating=? WHERE film_id=?",
                film.getName(), film.getDescription(), film.getDuration(),
                film.getReleaseDate(), film.getRating(), id);
        return film;
    }

    @Override
    public void deleteFilm(int id) {
        jdbcTemplate.update("DELETE FROM films WHERE film_id = ?", id);
    }
}
