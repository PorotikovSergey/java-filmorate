package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Types;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Collection;

@Component
@Slf4j
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

//    @Override
//    public Film addFilm(Film film) {
//        validate(film);
//        String sqlQuery = "INSERT INTO FILMS " +
//                "(FILM_NAME, FILM_DESCRIPTION, FILM_RELEASEDATE, FILM_DURATION, FILM_RATING) " +
//                "VALUES (?,?,?,?,?)";
//
//        jdbcTemplate.update(sqlQuery,
//                film.getName(),
//                film.getDescription(),
//                film.getReleaseDate(),
//                film.getDuration(),
//                film.getRating());
//
//        return film;
//    }

    @Override
    public Film addFilm(Film film) {
        validate(film);
        String sqlQuery = "INSERT INTO FILMS " +
                "(FILM_NAME, FILM_DESCRIPTION, FILM_RELEASEDATE, FILM_DURATION, FILM_RATING) " +
                "VALUES (?,?,?,?,?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"FILM_ID"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            final LocalDate releaseDate = film.getReleaseDate();
            if (releaseDate == null) {
                stmt.setNull(3, Types.DATE);
            } else {
                stmt.setDate(3, Date.valueOf(releaseDate));
            }
            stmt.setInt(4, film.getDuration());
            stmt.setInt(5, film.getRating());
            return stmt;
        }, keyHolder);
        film.setId(keyHolder.getKey().intValue());
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
        if(getFilmById(film.getId())==null) {
            throw new NotFoundException("Фильма с таким id не существует");
        }
        validate(film);
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

    //-------------------Проверка фильма на соотвтетствие-----------------------------------------
    private void validate(Film film) throws ValidationException{

        final int MAX_DESCRIPTION_LENGTH = 200;
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-d");
        final LocalDate FIRST_CINEMA_DATE = LocalDate.of(1895, 12, 28);


        if (film.getId() < 0) {
            throw new ValidationException("Id фильма не может быть отрицательным. " +
                    "Вы пытаетесь задать id: " + film.getId());
        }
        if (film.getName().isBlank()) {
            throw new ValidationException("Невозможно запостить фильм c пустым названием.");
        }
        if (film.getDescription().length() > MAX_DESCRIPTION_LENGTH) {
            throw new ValidationException("Невозможно запостить фильм  с описанием больше "
                    + MAX_DESCRIPTION_LENGTH + " символов");
        }
        if (film.getReleaseDate().isBefore(FIRST_CINEMA_DATE)) {
            throw new ValidationException("Невозможно запостить фильм с датой выпуска раньше " + FIRST_CINEMA_DATE);
        }
        if (film.getDuration() < 0) {
            throw new ValidationException("Невозможно запостить фильм с отрицательной длительностью: "
                    + film.getDuration());
        }
    }
}
