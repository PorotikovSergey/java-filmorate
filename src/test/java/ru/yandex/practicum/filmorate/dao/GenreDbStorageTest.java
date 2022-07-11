package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class GenreDbStorageTest {
    private final GenreDbStorage genreDbStorage;

    @Transactional
    @Test
    void getAll() {
        List<Genre> genreList = (ArrayList<Genre>) genreDbStorage.getAll();
        assertEquals(6, genreList.size(), "Всего должно быть 6 жанров");
        assertEquals("Комедия", genreList.get(0).getName(), "Правильный жанр - Комедия");
        assertEquals("Драма", genreList.get(1).getName(), "Правильный жанр - Драма");
        assertEquals("Мультфильм", genreList.get(2).getName(), "Правильный жанр - Мультфильм");
        assertEquals("Триллер", genreList.get(3).getName(), "Правильный жанр - Триллер");
        assertEquals("Документальный", genreList.get(4).getName(), "Правильный жанр - Документальный");
        assertEquals("Боевик", genreList.get(5).getName(), "Правильный жанр - Боевик");

    }

    @Transactional
    @Test
    void getById() {
        Genre genre = genreDbStorage.getGenreById(3);
        assertEquals("Мультфильм", genre.getName(), "Правильный жанр - Мультфильм");
    }
}