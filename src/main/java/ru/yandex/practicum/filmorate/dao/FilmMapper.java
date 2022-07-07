package ru.yandex.practicum.filmorate.dao;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FilmMapper implements RowMapper<Film> {
    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Film(
                rs.getString("FILM_NAME"),
                rs.getString("FILM_DESCRIPTION"),
                rs.getDate("FILM_RELEASEDATE").toLocalDate(),
                rs.getInt("FILM_DURATION"),
                rs.getInt("FILM_RATING"));
    }
}
