package ru.yandex.practicum.filmorate.dao;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = new User(
                rs.getString("USER_EMAIL"),
                rs.getString("USER_LOGIN"),
                rs.getString("USER_NAME"),
                rs.getDate("USER_BIRTHDAY").toLocalDate());
        user.setId(rs.getInt("USER_ID"));
        return user;
    }
}
