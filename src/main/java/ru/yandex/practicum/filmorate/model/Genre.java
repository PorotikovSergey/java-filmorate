package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class Genre {
    private int id;

    public Genre(int id, int rate) {
        this.id=id;
    }
    public Genre() {
    }
}
