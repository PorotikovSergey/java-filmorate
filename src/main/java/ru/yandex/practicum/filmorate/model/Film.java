package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;

import lombok.Data;

@Data
public class Film {
    private int id = 1;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private long duration;

}
