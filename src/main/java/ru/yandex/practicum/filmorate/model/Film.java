package ru.yandex.practicum.filmorate.model;

import java.util.Date;

import lombok.Data;

@Data
public class Film {
    private int id;
    private String name;
    private String description;
    private Date releaseDate;
    private long duration;
    private Rating rating;

    public Film(int id, String name, String description, Date releaseDate, long duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }
}
