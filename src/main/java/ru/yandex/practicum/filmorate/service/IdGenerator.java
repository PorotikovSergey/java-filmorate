package ru.yandex.practicum.filmorate.service;

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
}
