package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserDbStorageTest {
    private final UserDbStorage userStorage;

    @Test
    void setAndBreakFiendship() {
        User testUser1 = new User("mymail@mail.ru", "userLogin", "Vasya",
                LocalDate.of(1991, 8, 12));
        User testUser2 = new User("fresh@yandex.ru", "fresher2000", "Vova",
                LocalDate.of(2001, 1, 11));
        userStorage.addUser(testUser1);
        userStorage.addUser(testUser2);
        userStorage.setFriendship(1, 2);

        Collection<User> friends = userStorage.getAllConfirmedFriends(1);
        assertTrue(friends.contains(testUser2), "Возвращённый юзер должен быть равен обновленному");

        userStorage.breakFriendship(1, 2);
        Collection<User> friendsAfterDeleteOne = userStorage.getAllConfirmedFriends(1);
        assertFalse(friendsAfterDeleteOne.contains(testUser2), "Возвращённый юзер должен быть равен обновленному");
    }

    @Test
    void getCommonFriends() {
        User testUser3 = new User("mymail@mail.ru", "userLogin", "Vasya",
                LocalDate.of(1991, 8, 12));
        User testUser4 = new User("fresh@yandex.ru", "fresher2000", "Vova",
                LocalDate.of(2001, 1, 11));
        User testUser5 = new User("asd@mail.ru", "newUser", "Petya",
                LocalDate.of(1961, 5, 10));
        User testUser6 = new User("qwerty@gmail.com", "newUser2", "Misha",
                LocalDate.of(2011, 12, 23));
        userStorage.addUser(testUser3);
        userStorage.addUser(testUser4);
        userStorage.addUser(testUser5);
        userStorage.addUser(testUser6);

        userStorage.setFriendship(3, 4);
        userStorage.setFriendship(3, 5);
        userStorage.setFriendship(6, 4);

        Collection<User> commonFriends = userStorage.getCommonFriends(3, 6);
        assertEquals(1, commonFriends.size(),  "Должен быть всего 1 общий друг");
        assertTrue(commonFriends.contains(testUser4), "Общим должен быть Vova");
    }

    @Test
    void createAndReturnUserById() {
        User testUser = new User("mymail@mail.ru", "userLogin", "Vasya",
                LocalDate.of(1991, 8, 12));
        userStorage.addUser(testUser);
        User returnUser = userStorage.getUserById(7);
        assertEquals(testUser, returnUser, "Возвращённый юзер должен быть равен изначально созданному");
    }

    @Test
    void refreshAndReturnUserById() {
        User testUser = new User("mymail@mail.ru", "userLogin", "Vasya",
                LocalDate.of(1991, 8, 12));
        userStorage.addUser(testUser);

        User refreshedUser = new User("fresh@yandex.ru", "fresher2000", "Vova",
                LocalDate.of(2001, 1, 11));
        refreshedUser.setId(8);

        userStorage.modifyUser(refreshedUser);
        User returnUser = userStorage.getUserById(8);
        assertEquals(refreshedUser, returnUser, "Возвращённый юзер должен быть равен обновленному");
    }

    @Test
    void deleteUserById() {
        User testUser = new User("mymail@mail.ru", "userLogin", "Vasya",
                LocalDate.of(1991, 8, 12));
        userStorage.addUser(testUser);
        Collection<User> allUsers = userStorage.getAll();

        assertEquals(1, allUsers.size(), "В списке должен быть 1 пользователь");

        userStorage.deleteUser(9);
        Collection<User> allUsersAfterDeleteOne = userStorage.getAll();
        assertEquals(0, allUsersAfterDeleteOne.size(),  "В списке не должно остаться пользователей");
    }

}