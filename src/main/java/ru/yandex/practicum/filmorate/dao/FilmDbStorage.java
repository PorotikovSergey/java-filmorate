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
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Types;
import java.time.LocalDate;
import java.util.*;

@Component
@Slf4j
public class FilmDbStorage implements FilmStorage {
    static final String NO_SUCH_FILM = "Фильма с таким id не существует";
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film addFilm(Film film) {
        validate(film);
        String addGenre = "INSERT INTO FILM_GENRE_ACCORDING (FILM_ID, GENRE_ID) VALUES (?, ?)";
        String addMPA = "INSERT INTO FILM_MPA_ACCORDING (FILM_ID, MPA_ID) VALUES (?, ?)";
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
            stmt.setInt(5, film.getRate());
            return stmt;
        }, keyHolder);
        int key = keyHolder.getKey().intValue();
        film.setId(key);
        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                jdbcTemplate.update(addGenre, film.getId(), genre.getId());
            }
        }
        jdbcTemplate.update(addMPA, film.getId(), film.getMpa().getId());
        return film;
    }

    @Override
    public Film getFilmById(int id) {
        String sqlQuery = "SELECT * FROM FILMS WHERE FILM_ID = ?";

        Film film = jdbcTemplate.query(sqlQuery, new FilmMapper(), id)
                .stream().findAny().orElse(null);
        if (film == null) {
            throw new NotFoundException(NO_SUCH_FILM);
        }
        film.setMpa(getMPA(film.getId()));
        film.setGenres((LinkedHashSet<Genre>) getGenre(film.getId()));
        return film;
    }

    @Override
    public Film modifyFilm(Film film) {

        if (getFilmById(film.getId()) == null) {
            throw new NotFoundException(NO_SUCH_FILM);
        }

        if (!getGenre(film.getId()).isEmpty()) {
            jdbcTemplate.update("DELETE FROM FILM_GENRE_ACCORDING WHERE FILM_ID = ?", film.getId());
        }

        if (getMPA(film.getId()) != null) {
            jdbcTemplate.update("DELETE FROM FILM_MPA_ACCORDING WHERE FILM_ID = ?", film.getId());
        }

        validate(film);
        String sqlQuery = "UPDATE FILMS SET FILM_NAME = ?, FILM_DESCRIPTION = ?," +
                " FILM_RELEASEDATE = ?, FILM_DURATION = ?, FILM_RATING = ? WHERE FILM_ID = ?";
        String addMPA = "INSERT INTO FILM_MPA_ACCORDING (FILM_ID, MPA_ID) VALUES (?, ?)";
        String addGenre = "INSERT INTO FILM_GENRE_ACCORDING (FILM_ID, GENRE_ID) VALUES (?, ?)";

        jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getRate(),
                film.getId());

        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                jdbcTemplate.update(addGenre, film.getId(), genre.getId());
            }
        } else {
            film.setGenres(new LinkedHashSet<>());
        }
        if (film.getMpa() != null) {
            jdbcTemplate.update(addMPA, film.getId(), film.getMpa().getId());
        }
        return film;
    }

    @Override
    public Collection<Film> getAll() {
        return jdbcTemplate.query("SELECT * FROM FILMS", new FilmMapper());
    }

    @Override
    public void deleteFilm(int id) {
        if (getFilmById(id) == null) {
            throw new NotFoundException(NO_SUCH_FILM);
        }
        jdbcTemplate.update("DELETE FROM FILM_GENRE_ACCORDING WHERE FILM_ID = ?", id);
        jdbcTemplate.update("DELETE FROM FILM_MPA_ACCORDING WHERE FILM_ID = ?", id);
        jdbcTemplate.update("DELETE FROM LIKED_FILMS WHERE FILM_ID = ?", id);
        jdbcTemplate.update("DELETE FROM FILMS WHERE FILM_ID = ?", id);
    }

    @Override
    public void putLike(int filmId, int userId) {
        if (getFilmById(filmId) == null) {
            throw new NotFoundException(NO_SUCH_FILM);
        }
        if (getUserById(userId) == null) {
            throw new NotFoundException(NO_SUCH_FILM);
        }
        String sqlQuery = "INSERT INTO LIKED_FILMS (USER_ID, FILM_ID) VALUES (?, ?)";
        jdbcTemplate.update(sqlQuery, userId, filmId);
    }

    @Override
    public void deleteLike(int filmId, int userId) {
        if (getFilmById(filmId) == null) {
            throw new NotFoundException(NO_SUCH_FILM);
        }
        if (getUserById(userId) == null) {
            throw new NotFoundException("Юзера с таким id не существует");
        }
        jdbcTemplate.update("DELETE FROM LIKED_FILMS WHERE USER_ID=? AND FILM_ID=?", userId, filmId);
    }

    @Override
    public Collection<Film> getPopular(int count) {
        String testLike = "SELECT COUNT(*) FROM LIKED_FILMS";
        int testCount = jdbcTemplate.queryForObject(testLike, Integer.class);
        if (testCount == 0) {
            Collection<Film> resultList = jdbcTemplate.query(testLike, new FilmMapper(), count);
            for (Film film : resultList) {
                film.setMpa(getMPA(film.getId()));
                film.setGenres((LinkedHashSet<Genre>) getGenre(film.getId()));
            }
            return resultList;
        }
        String sqlQuery = "SELECT FILMS.FILM_ID, FILM_NAME, FILM_DESCRIPTION, " +
                "FILM_DURATION, FILM_RELEASEDATE, FILM_RATING " +
                "FROM FILMS WHERE FILM_ID IN " +
                "(SELECT FILM_ID FROM LIKED_FILMS GROUP BY USER_ID ORDER BY COUNT(FILM_ID)) LIMIT ?";
        Collection<Film> resultList = jdbcTemplate.query(sqlQuery, new FilmMapper(), count);
        for (Film film : resultList) {
            film.setMpa(getMPA(film.getId()));
            film.setGenres((LinkedHashSet<Genre>) getGenre(film.getId()));
        }
        return resultList;
    }

    @Override
    public Collection<Film> getAllPopular() {
        String testLike = "SELECT COUNT(*) FROM LIKED_FILMS";
        int testCount = jdbcTemplate.queryForObject(testLike, Integer.class);
        if (testCount == 0) {
            Collection<Film> resultList = getAll();
            for (Film film : resultList) {
                film.setMpa(getMPA(film.getId()));
                film.setGenres((LinkedHashSet<Genre>) getGenre(film.getId()));
            }
            return resultList;
        }
        String sqlQuery = "SELECT FILMS.FILM_ID, FILM_NAME, FILM_DESCRIPTION, " +
                "FILM_DURATION, FILM_RELEASEDATE, FILM_RATING " +
                "FROM FILMS WHERE FILM_ID IN " +
                "(SELECT FILM_ID FROM LIKED_FILMS GROUP BY USER_ID ORDER BY COUNT(FILM_ID))";
        Collection<Film> resultList = jdbcTemplate.query(sqlQuery, new FilmMapper());
        for (Film film : resultList) {
            film.setMpa(getMPA(film.getId()));
            film.setGenres((LinkedHashSet<Genre>) getGenre(film.getId()));
        }
        return resultList;
    }

    private void validate(Film film) throws ValidationException {

        final int MAX_DESCRIPTION_LENGTH = 200;
        final LocalDate firstCinemaDate = LocalDate.of(1895, 12, 28);


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
        if (film.getReleaseDate().isBefore(firstCinemaDate)) {
            throw new ValidationException("Невозможно запостить фильм с датой выпуска раньше " + firstCinemaDate);
        }
        if (film.getDuration() < 0) {
            throw new ValidationException("Невозможно запостить фильм с отрицательной длительностью: "
                    + film.getDuration());
        }
        if (film.getMpa() == null) {
            throw new ValidationException("Невозможно запостить фильм без мпа");
        }
    }

    public User getUserById(int id) {
        String sqlQuery = "SELECT * FROM USERS WHERE USER_ID = ?";

        User user = jdbcTemplate.query(sqlQuery, new UserMapper(), id)
                .stream().findAny().orElse(null);

        if (user == null) {
            throw new NotFoundException("Юзера с таким id не существует");
        } else {
            return user;
        }
    }

    public Mpa getMPA(int filmId) {
        String sqlQueryMPAId = "SELECT MPA_ID FROM FILM_MPA_ACCORDING WHERE FILM_ID = ?";
        int mpaId = jdbcTemplate.queryForObject(sqlQueryMPAId, Integer.class, filmId);

        String sqlQueryMPAFull = "SELECT * FROM MPA WHERE MPA_ID = ?";
        return jdbcTemplate.query(sqlQueryMPAFull, new MpaMapper(), mpaId).
                stream().findFirst().orElse(null);
    }

    public Collection<Genre> getGenre(int filmId) {
        Set<Genre> resultList = new LinkedHashSet<>();
        String sqlQueryGenreId = "SELECT GENRE_ID FROM FILM_GENRE_ACCORDING WHERE FILM_ID = ?";
        List<Integer> listOfGenres = jdbcTemplate.queryForList(sqlQueryGenreId, Integer.class, filmId);
        String sqlQueryGenreFull = "SELECT * FROM GENRE WHERE GENRE_ID = ?";
        for (int genreId : listOfGenres) {
            resultList.add(jdbcTemplate.query(sqlQueryGenreFull, new GenreMapper(), genreId).
                    stream().findFirst().orElse(null));
        }
        return resultList;
    }
}
