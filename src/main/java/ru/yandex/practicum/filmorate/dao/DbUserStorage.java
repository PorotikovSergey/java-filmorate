package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Types;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

@Component
@Repository
@Slf4j
public class DbUserStorage implements UserStorage {
    static final String NO_SUCH_USER = "Юзера с таким id не существует";
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DbUserStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User addUser(User user) {
        validate(user);
        String sqlQuery = "insert into USERS (USER_EMAIL, USER_LOGIN, USER_NAME, USER_BIRTHDAY) values (?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"USER_ID"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getName());
            final LocalDate birthday = user.getBirthday();
            if (birthday == null) {
                stmt.setNull(4, Types.DATE);
            } else {
                stmt.setDate(4, Date.valueOf(birthday));
            }
            return stmt;
        }, keyHolder);
        int key = keyHolder.getKey().intValue();
        user.setId(key);
        return user;
    }

    @Override
    public void deleteUser(int id) {
        String sqlQuery = "DELETE FROM USERS WHERE USER_ID = ?";
        jdbcTemplate.update(sqlQuery, id);
    }

    @Override
    public User getUserById(int id) {
        String sqlQuery = "SELECT * FROM USERS WHERE USER_ID = ?";

        User user = jdbcTemplate.query(sqlQuery, new UserMapper(), id)
                .stream().findAny().orElse(null);

        if (user == null) {
            throw new NotFoundException(NO_SUCH_USER);
        } else {
            return user;
        }
    }

    @Override
    public User modifyUser(User user) {
        if (getUserById(user.getId()) == null) {
            throw new NotFoundException(NO_SUCH_USER);
        }
        validate(user);
        String sqlQuery = "UPDATE USERS SET  USER_NAME = ?, USER_LOGIN = ?," +
                " USER_EMAIL = ?, USER_BIRTHDAY = ? WHERE USER_ID = ?";

        jdbcTemplate.update(sqlQuery,
                user.getName(),
                user.getLogin(),
                user.getEmail(),
                user.getBirthday(),
                user.getId());

        return user;
    }

    @Override
    public List<User> getAllConfirmedFriends(int id) {
        if (getUserById(id) == null) {
            throw new NotFoundException(NO_SUCH_USER);
        }
        String sql = "SELECT * FROM USERS WHERE USER_ID IN (SELECT FRIEND_ID FROM FRIENDSHIP WHERE USER_ID = ?)";
        return jdbcTemplate.query(sql, new UserMapper(), id);
    }

    @Override
    public List<User> getCommonFriends(int id, int otherId) {
        if (getUserById(id) == null) {
            throw new NotFoundException(NO_SUCH_USER);
        }
        if (getUserById(otherId) == null) {
            throw new NotFoundException(NO_SUCH_USER);
        }

        String sql = "SELECT * FROM USERS WHERE USER_ID IN " +
                "(SELECT FRIEND_ID FROM FRIENDSHIP WHERE USER_ID = ? and FRIEND_ID IN " +
                "(SELECT FRIEND_ID FROM FRIENDSHIP WHERE USER_ID = ?))";

        return jdbcTemplate.query(sql, new UserMapper(), id, otherId);
    }

    @Override
    public void setFriendship(int firstId, int secondId) {
        if (getUserById(firstId) == null) {
            throw new NotFoundException(NO_SUCH_USER);
        }
        if (getUserById(secondId) == null) {
            throw new NotFoundException(NO_SUCH_USER);
        }
        String sqlQuery = "INSERT INTO FRIENDSHIP (USER_ID, FRIEND_ID, STATUS)" +
                "VALUES (?,?,?)";

        jdbcTemplate.update(sqlQuery,
                firstId,
                secondId,
                true);
    }

    @Override
    public void breakFriendship(int firstId, int secondId) {
        String sqlQuery = "DELETE FROM FRIENDSHIP WHERE USER_ID = ? AND FRIEND_ID = ?";
        jdbcTemplate.update(sqlQuery, firstId, secondId);
    }

    @Override
    public Collection<User> getAll() {
        return jdbcTemplate.query("SELECT * FROM users", new UserMapper());
    }

    private void validate(User user) throws ValidationException {
        Pattern emailPattern = Pattern.compile(
                "\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*\\.\\w{2,4}");

        if (user.getId() < 0) {
            throw new ValidationException("Id юзера не может быть отрицательным. " +
                    "Вы пытаетесь задать id: " + user.getId());
        }
        if (!emailPattern.matcher(user.getEmail()).matches()) {
            throw new ValidationException("Email " + user.getEmail() + " не соответсвтует требованиям.");
        }
        if ((user.getLogin().isBlank()) || user.getLogin().contains(" ")) {
            throw new ValidationException("Логин " + user.getLogin() + " не соответствет требованиям.");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Указанная дата рождения " + user.getBirthday() + " находится в будущем.");
        }
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
