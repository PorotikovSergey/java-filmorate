package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmDbStorageTest {
    private final FilmDbStorage filmStorage;
    private final UserDbStorage userStorage;

    @Test
    void deleteFilms() {
        System.out.println("удаление");
        Film film = new Film("Интерстеллар", "Фильм про космос",
                LocalDate.of(2010, 10, 10), 120, 5);
        Mpa mpa = new Mpa();
        mpa.setId(1);
        film.setMpa(mpa);
        filmStorage.addFilm(film);

        Film film2 = new Film("Остин Пауэрс", "Комедия про шпиона",
                LocalDate.of(2000, 8, 23), 110, 4);
        Mpa mpa2 = new Mpa();
        mpa2.setId(2);
        film2.setMpa(mpa2);
        filmStorage.addFilm(film2);

        assertEquals(2, filmStorage.getAll().size(), "В списке должно быть 2 фильма");

        filmStorage.deleteFilm(1);
        assertEquals(1, filmStorage.getAll().size(), "В списке должен быть 1 фильм");

        filmStorage.deleteFilm(2);
        assertEquals(0, filmStorage.getAll().size(), "Список фильмов должен стать пустым");
    }

    @Test
    void createAndReturnFilmById() {
        System.out.println("создание");
        Film film = new Film("Интерстеллар", "Фильм про космос",
                LocalDate.of(2010, 10, 10), 120, 5);
        Mpa mpa = new Mpa();
        mpa.setId(1);
        film.setMpa(mpa);
        Set<Genre> genres = new LinkedHashSet<>();
        genres.add(new Genre(2, null));
        film.setGenres((LinkedHashSet<Genre>) genres);
        filmStorage.addFilm(film);

        Film returnFilm = filmStorage.getFilmById(3);
        assertEquals(film.getName(), returnFilm.getName(),
                "Возвращённый фильм должен быть равен изначально созданному");

        List<Genre> genreList = new ArrayList<>(filmStorage.getGenre(3));
        Mpa returnMpa = filmStorage.getMPA(3);

        assertEquals("Драма", genreList.get(0).getName(),
                "Возвращённый жанр должен быть Драма");
        assertEquals("G", returnMpa.getName(),
                "Возвращённое мпа должно быть G");
    }

    @Test
    void refreshAndReturnFilmById() {
        System.out.println("обновление");
        Film film = new Film("Интерстеллар", "Фильм про космос",
                LocalDate.of(2010, 10, 10), 120, 5);
        Mpa mpa = new Mpa();
        mpa.setId(1);
        film.setMpa(mpa);
        filmStorage.addFilm(film);

        Film returnFilm = filmStorage.getFilmById(4);
        assertEquals("Интерстеллар", returnFilm.getName(),
                "Возвращённый фильм должен быть Интерстеллар");

        Film refreshedFilm = new Film("Остин Пауэрс", "Комедия про шпиона",
                LocalDate.of(2000, 8, 23), 110, 4);
        Mpa mpa2 = new Mpa();
        mpa2.setId(2);
        refreshedFilm.setMpa(mpa2);
        refreshedFilm.setId(4);
        filmStorage.modifyFilm(refreshedFilm);

        Film returnFilm2 = filmStorage.getFilmById(4);
        assertEquals("Остин Пауэрс", returnFilm2.getName(),
                "Теперь возвращённый фильм должен быть Остин Пауэрс");
    }

    @Test
    void putLikeAndGetPopular() {
        System.out.println("лайк");
        User testUser1 = new User("mymail@mail.ru", "userLogin", "Vasya",
                LocalDate.of(1991, 8, 12));
        User testUser2 = new User("fresh@yandex.ru", "fresher2000", "Vova",
                LocalDate.of(2001, 1, 11));
        User testUser3 = new User("asd@mail.ru", "newUser", "Petya",
                LocalDate.of(1961, 5, 10));
        userStorage.addUser(testUser1);
        userStorage.addUser(testUser2);
        userStorage.addUser(testUser3);

        Film film = new Film("Интерстеллар", "Фильм про космос",
                LocalDate.of(2010, 10, 10), 120, 5);
        Mpa mpa = new Mpa();
        mpa.setId(1);
        film.setMpa(mpa);
        filmStorage.addFilm(film);

        Film film2 = new Film("Остин Пауэрс", "Комедия про шпиона",
                LocalDate.of(2000, 8, 23), 110, 4);
        Mpa mpa2 = new Mpa();
        mpa2.setId(2);
        film2.setMpa(mpa2);
        filmStorage.addFilm(film2);

        System.out.println(userStorage.getAll());

        filmStorage.putLike(5, 10);
        filmStorage.putLike(6, 11);
        filmStorage.putLike(5, 12);

        ArrayList<Film> resultListWithOneFilm = (ArrayList<Film>) filmStorage.getPopular(1);
        assertEquals("Интерстеллар", resultListWithOneFilm.get(0).getName(),
                "Самый популярный фильм должен быть Интерстеллар");
        ArrayList<Film> resultListWithAllPopularFilms = (ArrayList<Film>) filmStorage.getAllPopular();
        assertEquals(2, resultListWithAllPopularFilms.size(), "Всего должно быть 2 фильма");
        assertEquals("Интерстеллар", resultListWithAllPopularFilms.get(0).getName(),
                "Самый популярный фильм должен быть Интерстеллар");
        assertEquals("Остин Пауэрс", resultListWithAllPopularFilms.get(1).getName(),
                "Второй по популярности фильм должен быть Остин Пауэрс");

        filmStorage.deleteLike(5, 10);
        filmStorage.deleteLike(5, 12);
        ArrayList<Film> resultListWithDeletedLikes = (ArrayList<Film>) filmStorage.getAllPopular();
        assertEquals("Остин Пауэрс", resultListWithDeletedLikes.get(0).getName(),
                "Самым популярным фильмом должен стать Остин Пауэрс");
    }
}