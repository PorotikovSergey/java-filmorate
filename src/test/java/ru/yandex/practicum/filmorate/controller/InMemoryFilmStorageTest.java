package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.storage.FilmStorageTest;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

class InMemoryFilmStorageTest extends FilmStorageTest {
    public InMemoryFilmStorageTest() {
        super(new InMemoryFilmStorage());
    }
}