package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.nio.charset.StandardCharsets;
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
    public Film addFilm(Film film) {
        String sqlQuery = "INSERT INTO FILMS " +
                "(FILM_NAME, FILM_DESCRIPTION, FILM_RELEASEDATE, FILM_DURATION, FILM_RATING) " +
                "VALUES (?,?,?,?,?)";

        jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getRating());

        return film;
    }

    @Override
    public Film getFilmById(int id) {
        String sqlQuery = "SELECT * FROM FILMS WHERE FILM_ID = ?";

        return jdbcTemplate.query(sqlQuery, new Object[]{id}, new FilmMapper())
                .stream().findAny().orElse(null);
    }
    @Override
    public Film modifyFilm(Film film) {
        String sqlQuery = "UPDATE FILMS SET FILM_NAME = ?, FILM_DESCRIPTION = ?," +
                " FILM_RELEASEDATE = ?, FILM_DURATION = ?, FILM_RATING = ? WHERE FILM_ID = ?";

        jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getRating(),
                film.getId());

        return film;
    }

    @Override
    public Collection<Film> getAll() {
        return jdbcTemplate.query("SELECT * FROM FILMS", new FilmMapper());
    }

    @Override
    public void deleteFilm(int id) {
        jdbcTemplate.update("DELETE FROM FILMS WHERE FILM_ID = ?", id);
    }

    @Override
    public void putLike(int userId, int filmId) {
        String sqlQuery = "INSERT INTO LIKED_FILMS (USER_ID, FILM_ID) VALUES (?, ?)";
        jdbcTemplate.update(sqlQuery, userId, filmId);

    }

    @Override
    public void deleteLike(int userId, int filmId) {
        jdbcTemplate.update("DELETE FROM LIKED_FILMS WHERE USER_ID=? AND FILM_ID=?", userId, filmId);
    }
}
