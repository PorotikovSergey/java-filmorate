package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.IdGenerator;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private static final int MAX_DESCRIPTION_LENGTH = 200;
    private static final LocalDate FIRST_CINEMA_DATE = LocalDate.of(1895, 12, 28);
    private final Map<Integer, Film> films = new HashMap<>();

    @Override
    public Film addFilm(Film film) throws ValidationException {
        film.setId(IdGenerator.generateFilmId());
        validate(film);
        films.put(film.getId(), film);
        log.debug("Добавлен фильм: {}", film);
        log.debug("Размер мапы с фильмами: {}", films.size());
        return film;
    }

    @Override
    public Film deleteFilm(Film film) {
        if (films.containsKey(film.getId())) {
            films.remove(film.getId());
            return film;
        } else {
            log.debug("Фильма с Id {} не существует", film.getId());
            return null;        //Вот тут надо как-то по-другому! НЕ ЗАБЫТЬ ИСПРАВИТЬ!!!
        }
    }

    @Override
    public Film modifyFilm(Film film) throws ValidationException {
        validate(film);
        if (!films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            log.debug("Обновлён(добавлен) фильм: {}", film);
            log.debug("Размер мапы с фильмами-{} после обновления(добавления) фильма: {}", films.size(), film);
        } else {
            films.replace(film.getId(), film);
            log.debug("Обновлён фильм: {}", film);
            log.debug("Размер мапы с фильмами-{} после обновления фильма: {}", films.size(), film);
        }
        return film;
    }

    @Override
    public Collection<Film> getAll() {
        log.debug("Текущее количество фильмов: {}", films.size());
        return films.values();
    }

    //-----------------------Валидация-------------------------------------------------------------------------------
    private void validate(Film film) throws ValidationException {
        if (film.getId() < 0) {
            throw new ValidationException("Id фильма не может быть отрицательным. " +
                    "Вы пытаетесь задать id: " + film.getId());
        }
        if (film.getName().isBlank()) {
            throw new ValidationException("Невозможно запостить фильм c пустым названием.");
        }
        if (film.getDescription().length() > MAX_DESCRIPTION_LENGTH) {
            throw new ValidationException("Невозможно запостить фильм  с описанием больше "
                    + MAX_DESCRIPTION_LENGTH + " символов");
        }
        if (film.getReleaseDate().isBefore(FIRST_CINEMA_DATE)) {
            throw new ValidationException("Невозможно запостить фильм с датой выпуска раньше " + FIRST_CINEMA_DATE);
        }
        if (film.getDuration() < 0) {
            throw new ValidationException("Невозможно запостить фильм с отрицательной длительностью: "
                    + film.getDuration());
        }
    }
}
