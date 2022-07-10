package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.Collection;

@Component
@Repository
@Slf4j
public class MpaDbStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Mpa getMpaById(int id) {
        String sqlQueryMPAId = "SELECT * FROM MPA WHERE MPA_ID = ?";

        Mpa mpa = jdbcTemplate.query(sqlQueryMPAId, new Object[]{id}, new MpaMapper()).
                stream().findFirst().orElse(null);
        if(mpa==null) {
            throw new NotFoundException("Мпа с таким id не существует");
        } else {
            return mpa;
        }
    }

    @Override
    public Collection<Mpa> getAll() {
        return jdbcTemplate.query("SELECT * FROM MPA", new MpaMapper());
    }
}
