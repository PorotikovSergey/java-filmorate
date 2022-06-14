package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorageTest;

class InMemoryUserStorageTest extends UserStorageTest {
    public InMemoryUserStorageTest() {
        super(new InMemoryUserStorage());
    }
}