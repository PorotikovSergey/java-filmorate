package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;

@Component
@Slf4j
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User getUserById(int id) {
        return jdbcTemplate.query("select * from users where user_id = ?", new Object[]{id}, new UserMapper())
                .stream().findAny().orElse(null);
    }

    @Override
    public Collection<User> getAll() {
        return jdbcTemplate.query("SELECT * FROM users", new UserMapper());
    }

    @Override
    public Collection<Film> getAllLikedFilms(int id) {
        return jdbcTemplate.query("SELECT * from films WHERE film_id IN " +
                "(SELECT film_id FROM liked_films WHERE user_id =?)",
                new Object[]{id}, new FilmMapper());
    }

    @Override
    public Collection<User> getAllConfirmedFriends(int id) {
        return jdbcTemplate.query("SELECT * from users WHERE user_id IN " +
                "(SELECT friend_id FROM friendship WHERE user_id = ? AND status = true)",
                new Object[]{id}, new UserMapper());
    }
    @Override
    public Collection<User> getAllUnconfirmedFriends(int id) {
        return jdbcTemplate.query("SELECT * from users WHERE user_id IN " +
                        "(SELECT friend_id FROM friendship WHERE user_id = ? AND status = false)",
                new Object[]{id}, new UserMapper());
    }

    @Override
    public User addUser(User user) {
        jdbcTemplate.update("INSERT INTO users (USER_NAME, USER_LOGIN, USER_EMAIL, USER_BIRTHDAY) " +
                "VALUES (?,?,?,?)" , user.getName(), user.getLogin(), user.getEmail(), user.getBirthday());
        return user;
    }

    @Override
    public User modifyUser(int id, User user) {
        jdbcTemplate.update("UPDATE users SET user_name=?, user_login=?, " +
                "user_email=?, user_birthday=? WHERE user_id=?",
                user.getName(), user.getLogin(), user.getEmail(), user.getBirthday(), id);
        return user;
    }

    @Override
    public void deleteUser(int id) {
        jdbcTemplate.update("DELETE FROM users WHERE user_id = ?", id);
    }







}
