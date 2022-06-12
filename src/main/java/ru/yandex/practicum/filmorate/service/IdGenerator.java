package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;

@Service
public class IdGenerator {
    private static int filmId = 0;
    private static int userId = 0;

    private IdGenerator() {
    }

    public static int generateFilmId() {
        return ++filmId;
    }

    public static int generateUserId() {
        return ++userId;
    }

    public static void setFilmId(int filmId) {
        IdGenerator.filmId = filmId;
    }

    public static void setUserId(int userId) {
        IdGenerator.userId = userId;
    }
}
