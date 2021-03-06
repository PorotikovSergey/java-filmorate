package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

import lombok.Data;

@Data
public class Film {
    private int id=1;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
    private int rate;
    private Mpa mpa;
    private Set<Genre> genres = new TreeSet<>(Comparator.comparingInt(Genre::getId));

    public Film(String name, String description, LocalDate releaseDate, int duration, int rate) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.rate = rate;
    }

}
