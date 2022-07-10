package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpa;

@Component
@Repository
@Slf4j
public class MpaDbStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Mpa getMpaById(int id) {
        String sqlQueryMPAId = "SELECT * FROM MPA WHERE MPA_ID = ?";

        return jdbcTemplate.query(sqlQueryMPAId, new Object[]{id}, new MpaMapper()).
                stream().findFirst().orElse(null);
    }
}
