package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class MPA {
    private int id;

    public MPA(int id, int rate) {
        this.id=id;
    }
    public MPA() {
    }
}
