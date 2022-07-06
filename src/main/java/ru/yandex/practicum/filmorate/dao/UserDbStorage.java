package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Types;
import java.time.LocalDate;
import java.util.Collection;

@Component
@Repository
@Slf4j
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

   @Override
    public User addUser(User user) {
        String sqlQuery = "INSERT INTO USERS (USER_NAME, USER_LOGIN, USER_EMAIL, USER_BIRTHDAY)" +
                "VALUES (?,?,?,?)";

        jdbcTemplate.update(sqlQuery,
                user.getName(),
                user.getLogin(),
                user.getEmail(),
                user.getBirthday());

        return user;
    }
//    @Override
//    public User addUser(User user) {
//        String sqlQuery = "insert into USERS (USER_EMAIL, USER_LOGIN, USER_NAME, USER_BIRTHDAY) values (?, ?, ?, ?)";
//
//        KeyHolder keyHolder = new GeneratedKeyHolder();
//        jdbcTemplate.update(connection -> {
//            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"USER_ID"});
//            stmt.setString(1, user.getEmail());
//            stmt.setString(2, user.getLogin());
//            stmt.setString(3, user.getName());
//            final LocalDate birthday = user.getBirthday();
//            if (birthday == null) {
//                stmt.setNull(4, Types.DATE);
//            } else {
//                stmt.setDate(4, Date.valueOf(birthday));
//            }
//            return stmt;
//        }, keyHolder);
//        user.setId(keyHolder.getKey().intValue());
//        return user;
//    }

    @Override
    public void deleteUser(int id) {
        String sqlQuery = "DELETE FROM USERS WHERE USER_ID = ?";
        jdbcTemplate.update(sqlQuery, id);
    }

    @Override
    public User getUserById(int id) {
        String sqlQuery = "SELECT * FROM USERS WHERE USER_ID = ?";

        return jdbcTemplate.query(sqlQuery, new Object[]{id}, new UserMapper())
                .stream().findAny().orElse(null);
    }

    @Override
    public User modifyUser(User user) {
        String sqlQuery = "UPDATE USERS SET USER_ID = ? , USER_NAME = ?, USER_LOGIN = ?," +
                " USER_EMAIL = ?, USER_BIRTHDAY = ? WHERE USER_ID = ?";

        jdbcTemplate.update(sqlQuery,
                user.getId(),
                user.getName(),
                user.getLogin(),
                user.getEmail(),
                user.getBirthday(),
                user.getId());

        return user;
    }

    @Override
    public Collection<User> getAll() {
        return jdbcTemplate.query("SELECT * FROM users", new UserMapper());
    }

}
