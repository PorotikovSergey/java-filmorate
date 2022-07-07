package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;

import lombok.Data;

@Data
public class Film {
    private int id=1;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
    private String rating;

    public Film(String name, String description, LocalDate releaseDate, int duration, String rating) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.rating = rating;
    }
}
