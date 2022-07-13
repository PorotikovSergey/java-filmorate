package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class MpaDbStorageTest {
    private final MpaDbStorage mpaDbStorage;

    @Test
    void getAll() {
        List<Mpa> mpaList = (ArrayList<Mpa>) mpaDbStorage.getAll();
        assertEquals(5, mpaList.size(), "Всего должно быть 5 мпа");
        assertEquals("G", mpaList.get(0).getName(), "Правильный мпа -G");
        assertEquals("PG", mpaList.get(1).getName(), "Правильный мпа -PG");
        assertEquals("PG-13", mpaList.get(2).getName(), "Правильный мпа -PG-13");
        assertEquals("R", mpaList.get(3).getName(), "Правильный мпа -R");
        assertEquals("NC-17", mpaList.get(4).getName(), "Правильный мпа -NC-17");
    }

    @Test
    void getById() {
        Mpa mpa = mpaDbStorage.getMpaById(3);
        assertEquals("PG-13", mpa.getName(), "Правильный мпа -PG-13");
    }
}