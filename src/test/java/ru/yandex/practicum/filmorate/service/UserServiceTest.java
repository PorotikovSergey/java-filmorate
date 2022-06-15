package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {
    private final UserService userService = new UserService(new InMemoryUserStorage());

    @BeforeEach
    void clear() {
        IdGenerator.setUserId(0);
    }

    @Test
    void setFriendship() {
        User user = new User();
        user.setName("Вася");
        user.setLogin("Нагибатор3000");
        user.setEmail("nagibator@mail.ru");
        user.setBirthday(LocalDate.of(1999, 6, 26));
        userService.addUser(user);

        User user2 = new User();
        user2.setName("Вася2");
        user2.setLogin("Нагибатор3000");
        user2.setEmail("nagibator@mail.ru");
        user2.setBirthday(LocalDate.of(1999, 6, 26));
        userService.addUser(user2);

        User user3 = new User();
        user3.setName("Вася2");
        user3.setLogin("Нагибатор3000");
        user3.setEmail("nagibator@mail.ru");
        user3.setBirthday(LocalDate.of(1999, 6, 26));
        userService.addUser(user3);

        userService.setFriendship(1, 2);
        userService.setFriendship(1, 3);
        assertEquals(2, user.getFriends().size(), "В списке должны быть 2 дргуа");
        assertEquals(1, user2.getFriends().size(), "В списке должен быть 1 друг");
        assertEquals(1, user3.getFriends().size(), "В списке должен быть 1 друг");
    }

    @Test
    void breakFriendship() {
        User user = new User();
        user.setName("Вася");
        user.setLogin("Нагибатор3000");
        user.setEmail("nagibator@mail.ru");
        user.setBirthday(LocalDate.of(1999, 6, 26));
        userService.addUser(user);

        User user2 = new User();
        user2.setName("Вася2");
        user2.setLogin("Нагибатор3000");
        user2.setEmail("nagibator@mail.ru");
        user2.setBirthday(LocalDate.of(1999, 6, 26));
        userService.addUser(user2);

        User user3 = new User();
        user3.setName("Вася2");
        user3.setLogin("Нагибатор3000");
        user3.setEmail("nagibator@mail.ru");
        user3.setBirthday(LocalDate.of(1999, 6, 26));
        userService.addUser(user3);

        userService.setFriendship(1, 2);
        userService.setFriendship(1, 3);

        userService.breakFriendship(1, 2);
        assertEquals(1, user.getFriends().size(), "В списке должен остаться 1 друг");
        assertEquals(0, user2.getFriends().size(), "В списке должно остаться 0 друзей");
        userService.breakFriendship(1, 3);
        assertEquals(0, user.getFriends().size(), "В списке должно остаться 0 друзей");
        assertEquals(0, user3.getFriends().size(), "В списке должно остаться 0 друзей");
    }

    @Test
    void getAllFriends() {
        User user = new User();
        user.setName("Вася");
        user.setLogin("Нагибатор3000");
        user.setEmail("nagibator@mail.ru");
        user.setBirthday(LocalDate.of(1999, 6, 26));
        userService.addUser(user);

        User user2 = new User();
        user2.setName("Вася2");
        user2.setLogin("Нагибатор3000");
        user2.setEmail("nagibator@mail.ru");
        user2.setBirthday(LocalDate.of(1999, 6, 26));
        userService.addUser(user2);

        User user3 = new User();
        user3.setName("Вася2");
        user3.setLogin("Нагибатор3000");
        user3.setEmail("nagibator@mail.ru");
        user3.setBirthday(LocalDate.of(1999, 6, 26));
        userService.addUser(user3);

        userService.setFriendship(1, 2);
        userService.setFriendship(1, 3);

        assertTrue(userService.getAllFriends(1).contains(user2), "В списке должен быть друг с id2");
        assertTrue(userService.getAllFriends(1).contains(user3), "В списке должен быть друг с id3");
        assertTrue(userService.getAllFriends(2).contains(user), "В списке у второго юзера должен быть друг с id1");
        assertTrue(userService.getAllFriends(3).contains(user), "В списке у третьего юзера должен быть друг с id1");
    }

    @Test
    void getCommonFriends() {
        User user = new User();
        user.setName("Вася");
        user.setLogin("Нагибатор3000");
        user.setEmail("nagibator@mail.ru");
        user.setBirthday(LocalDate.of(1999, 6, 26));
        userService.addUser(user);

        User user2 = new User();
        user2.setName("Вася2");
        user2.setLogin("Нагибатор3000");
        user2.setEmail("nagibator@mail.ru");
        user2.setBirthday(LocalDate.of(1999, 6, 26));
        userService.addUser(user2);

        User user3 = new User();
        user3.setName("Вася2");
        user3.setLogin("Нагибатор3000");
        user3.setEmail("nagibator@mail.ru");
        user3.setBirthday(LocalDate.of(1999, 6, 26));
        userService.addUser(user3);

        User user4 = new User();
        user4.setName("Вася");
        user4.setLogin("Нагибатор3000");
        user4.setEmail("nagibator@mail.ru");
        user4.setBirthday(LocalDate.of(1999, 6, 26));
        userService.addUser(user4);

        User user5 = new User();
        user5.setName("Вася2");
        user5.setLogin("Нагибатор3000");
        user5.setEmail("nagibator@mail.ru");
        user5.setBirthday(LocalDate.of(1999, 6, 26));
        userService.addUser(user5);

        User user6 = new User();
        user6.setName("Вася2");
        user6.setLogin("Нагибатор3000");
        user6.setEmail("nagibator@mail.ru");
        user6.setBirthday(LocalDate.of(1999, 6, 26));
        userService.addUser(user6);

        userService.setFriendship(1, 2);
        userService.setFriendship(1, 3);
        userService.setFriendship(1, 4);

        userService.setFriendship(6, 3);
        userService.setFriendship(6, 4);
        userService.setFriendship(6, 5);

        assertEquals(2, userService.getCommonFriends(1, 6).size(),
                "Общих друзей должно быть 2");
        System.out.println(userService.getCommonFriends(1, 6));
        assertTrue(userService.getCommonFriends(1, 6).contains(user3),
                "В списке общих друзей должен быть друг с id3");
        assertTrue(userService.getCommonFriends(1, 6).contains(user4),
                "В списке общих друзей должен быть друг с id4");
        assertFalse(userService.getCommonFriends(1, 6).contains(user),
                "В списке общих друзей не должен быть друг с id1");
        assertFalse(userService.getCommonFriends(1, 6).contains(user),
                "В списке общих друзей не должен быть друг с id5");
    }

    @Test
    void getAllAndThenRemove() {
        User user = new User();
        user.setName("Вася");
        user.setLogin("Нагибатор3001");
        user.setEmail("nagibator@mail.ru");
        user.setBirthday(LocalDate.of(1999, 6, 26));
        userService.addUser(user);

        User user2 = new User();
        user2.setName("Вася2");
        user2.setLogin("Нагибатор3002");
        user2.setEmail("nagibator@mail.ru");
        user2.setBirthday(LocalDate.of(1998, 6, 26));
        userService.addUser(user2);

        User user3 = new User();
        user3.setName("Вася3");
        user3.setLogin("Нагибатор3003");
        user3.setEmail("nagibator@mail.ru");
        user3.setBirthday(LocalDate.of(1997, 6, 26));
        userService.addUser(user3);

        User user4 = new User();
        user4.setName("Вася4");
        user4.setLogin("Нагибатор3004");
        user4.setEmail("nagibator@mail.ru");
        user4.setBirthday(LocalDate.of(1996, 6, 26));
        userService.addUser(user4);

        User user5 = new User();
        user5.setName("Вася5");
        user5.setLogin("Нагибатор3005");
        user5.setEmail("nagibator@mail.ru");
        user5.setBirthday(LocalDate.of(1995, 6, 26));
        userService.addUser(user5);

        User user6 = new User();
        user6.setName("Вася6");
        user6.setLogin("Нагибатор3006");
        user6.setEmail("nagibator@mail.ru");
        user6.setBirthday(LocalDate.of(1994, 6, 26));
        userService.addUser(user6);

        assertEquals(6, userService.getAll().size(), "Юзеров должно быть 6");
        userService.deleteUser(1);
        assertEquals(5, userService.getAll().size(), "Юзеров должно стать 5");
        userService.deleteUser(2);
        userService.deleteUser(3);
        userService.deleteUser(4);
        userService.deleteUser(5);
        userService.deleteUser(6);
        assertTrue(userService.getAll().isEmpty(), "Юзеров должно не остаться");
    }

    @Test
    void modifyUsers() {
        User user = new User();
        user.setName("Вася");
        user.setLogin("Нагибатор3001");
        user.setEmail("nagibator@mail.ru");
        user.setBirthday(LocalDate.of(1999, 6, 26));
        userService.addUser(user);

        User user2 = new User();
        user2.setName("Вася2");
        user2.setLogin("Нагибатор3002");
        user2.setEmail("nagibator@mail.ru");
        user2.setBirthday(LocalDate.of(1998, 6, 26));
        userService.addUser(user2);

        assertEquals(2, userService.getAll().size(), "Юзеров должно быть 2");
        assertEquals("Вася", userService.getUserById(1).getName(),
                "Имя юзера должно быть - 'Вася'");

        user2.setId(1);
        userService.modifyUser(user2);
        assertEquals("Вася2", userService.getUserById(1).getName(),
                "Имя юзера должно стать - 'Вася2'");
        assertEquals(2, userService.getAll().size(),
                "После модификации юзера их общее количсетво не должно измениться");
    }
}