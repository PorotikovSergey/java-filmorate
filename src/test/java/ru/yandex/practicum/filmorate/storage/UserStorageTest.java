package ru.yandex.practicum.filmorate.storage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.IdGenerator;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public abstract class UserStorageTest <T extends UserStorage> {
    private final T userStorage;

    public UserStorageTest(UserStorage userStorage) {
        this.userStorage = (T) userStorage;
    }

    @BeforeEach
    void clear() {
        IdGenerator.setUserId(0);
    }

    @Test
    void createUserRight() throws ValidationException {
        User user = new User();
        user.setName("Вася");
        user.setLogin("Нагибатор3000");
        user.setEmail("nagibator@mail.ru");
        user.setBirthday(LocalDate.of(1999, 6, 26));
        userStorage.addUser(user);
        assertEquals("Вася", user.getName(), "Имена не совпадает");
        assertEquals("Нагибатор3000", user.getLogin(), "Логины не совпадает");
        assertEquals("nagibator@mail.ru", user.getEmail(), "Длительность не совпадает");
        assertEquals(1, user.getId(), "Id не совпадает");
        assertEquals("1999-06-26", user.getBirthday().toString(), "День рожденья не совпадает");
    }

    @Test
    void createUserWithEmptyNameRight() throws ValidationException {
        User user = new User();
        user.setName("");
        user.setLogin("Нагибатор3001");
        user.setEmail("nagibator@mail.com");
        user.setBirthday(LocalDate.of(1998, 10, 26));
        userStorage.addUser(user);
        assertEquals("Нагибатор3001", user.getName(), "Пустое имя должно было смениться на логин");
        assertEquals("Нагибатор3001", user.getLogin(), "Логин должен был остаться прежним");
    }

    @Test
    void doNotCreateUserWithWrongEmail() {
        User user = new User();
        user.setName("Петя");
        user.setLogin("Мамколюб1997");
        user.setEmail("motherloverAmail.com");
        user.setBirthday(LocalDate.of(1997, 10, 26));
        Throwable thrown = assertThrows(ValidationException.class, () -> {
            userStorage.addUser(user);
        });
        assertNotNull(thrown.getMessage(), "Нельзя добавить юзера с неправильным email: " + user.getEmail());
    }

    @Test
    void doNotCreateUserFromFuture() {
        User user = new User();
        user.setName("Алёша");
        user.setLogin("AlEx");
        user.setEmail("alexey@gmail.com");
        user.setBirthday(LocalDate.of(3000, 10, 26));
        Throwable thrown2 = assertThrows(ValidationException.class, () -> {
            userStorage.addUser(user);
        });
        assertNotNull(thrown2.getMessage(), "Нельзя добавить юзера из будущего");
    }

    @Test
    void refreshUsersRight() throws ValidationException {
        User user = new User();
        user.setName("Вася");
        user.setLogin("Нагибатор3000");
        user.setEmail("nagibator@mail.ru");
        user.setBirthday(LocalDate.of(1999, 6, 26));
        userStorage.addUser(user);

        User newUser = new User();
        newUser.setId(user.getId());
        newUser.setName("Петя");
        newUser.setLogin("Нагибатор3333");
        newUser.setEmail("nagibator3333@mail.com");
        newUser.setBirthday(LocalDate.of(2000, 10, 21));
        userStorage.modifyUser(newUser);
        assertFalse(userStorage.getAll().contains(user), "Юзера не должно было остаться");
        assertTrue(userStorage.getAll().contains(newUser), "Юзер2 не должен был заменить юзера");
        assertEquals(user.getId(), newUser.getId(), "Id у юзера2 должен был стать у первого юзера раньше");
    }

    @Test
    void refreshUserWithNewIdRight() throws ValidationException {
        User user = new User();
        user.setName("Вася");
        user.setLogin("Нагибатор3000");
        user.setEmail("nagibator@mail.ru");
        user.setBirthday(LocalDate.of(1999, 6, 26));
        userStorage.addUser(user);

        User newUser = new User();
        newUser.setName("Миша");
        newUser.setLogin("Нагибатор1");
        newUser.setEmail("nagibator1@mail.com");
        newUser.setBirthday(LocalDate.of(2001, 11, 11));
        userStorage.addUser(newUser);

        assertTrue(userStorage.getAll().contains(user), "Юзера должен был остаться");
        assertTrue(userStorage.getAll().contains(newUser), "Юзер3 должен был просто добавиться");
    }

    @Test
    void refreshUserWithEmptyName() throws ValidationException {
        User user = new User();
        user.setName("Вася");
        user.setLogin("Нагибатор3000");
        user.setEmail("nagibator@mail.ru");
        user.setBirthday(LocalDate.of(1999, 6, 26));
        userStorage.addUser(user);

        User newUser = new User();
        newUser.setId(user.getId());
        newUser.setName("");
        newUser.setLogin("Нагибаторщик");
        newUser.setEmail("nagibator@69mail.com");
        newUser.setBirthday(LocalDate.of(2001, 11, 11));
        userStorage.modifyUser(newUser);

        assertEquals("Нагибаторщик", newUser.getName(), "Пустое имя должно было смениться на логин");
        assertFalse(userStorage.getAll().contains(user), "Юзер должен был удалиться");
        assertTrue(userStorage.getAll().contains(newUser), "Юзер4 должен был просто добавиться вместо первого");
    }

    @Test
    void refreshUserWithWrongEmail() throws ValidationException {
        User user = new User();
        user.setName("Вася");
        user.setLogin("Нагибатор3000");
        user.setEmail("nagibator@mail.ru");
        user.setBirthday(LocalDate.of(1999, 6, 26));
        userStorage.addUser(user);

        User newUser = new User();
        newUser.setName("Володя");
        newUser.setLogin("Нагибаторщик");
        newUser.setEmail("nagibator69mail.com");
        newUser.setBirthday(LocalDate.of(2001, 11, 11));
        Throwable thrown = assertThrows(ValidationException.class, () -> {
            userStorage.addUser(newUser);
        });

        assertNotNull(thrown.getMessage(), "Нельзя обновить юзера с неправильной почтой");
        assertFalse(userStorage.getAll().contains(newUser), "Юзер5 не должен был просто добавиться");
    }

    @Test
    void returnRightSize() throws ValidationException {
        User user = new User();
        user.setName("Вася");
        user.setLogin("Нагибатор3000");
        user.setEmail("nagibator@mail.ru");
        user.setBirthday(LocalDate.of(1999, 6, 26));
        userStorage.addUser(user);

        User newUser = new User();
        newUser.setName("Петя");
        newUser.setLogin("Нагибатор2000");
        newUser.setEmail("nagibator2000@mail.ru");
        newUser.setBirthday(LocalDate.of(1998, 6, 26));
        userStorage.addUser(newUser);

        User oneMoreUser = new User();
        oneMoreUser.setName("");
        oneMoreUser.setLogin("Нагибатор3000");
        oneMoreUser.setEmail("nagibator1000@mail.ru");
        oneMoreUser.setBirthday(LocalDate.of(1997, 6, 26));
        userStorage.addUser(oneMoreUser);

        assertEquals(3, userStorage.getAll().size(), "В списке должно быть 3 юзера");
    }

    @Test
    void removeUsers() throws ValidationException {
        User user = new User();
        user.setName("Вася");
        user.setLogin("Нагибатор3000");
        user.setEmail("nagibator@mail.ru");
        user.setBirthday(LocalDate.of(1999, 6, 26));
        userStorage.addUser(user);

        User newUser = new User();
        newUser.setName("Петя");
        newUser.setLogin("Нагибатор2000");
        newUser.setEmail("nagibator2000@mail.ru");
        newUser.setBirthday(LocalDate.of(1998, 6, 26));
        userStorage.addUser(newUser);

        User oneMoreUser = new User();
        oneMoreUser.setName("");
        oneMoreUser.setLogin("Нагибатор3000");
        oneMoreUser.setEmail("nagibator1000@mail.ru");
        oneMoreUser.setBirthday(LocalDate.of(1997, 6, 26));
        userStorage.addUser(oneMoreUser);

        userStorage.deleteUser(1);
        assertEquals(2, userStorage.getAll().size(), "Список должен состоять из 2 юзеров");
        userStorage.deleteUser(2);
        assertEquals(1, userStorage.getAll().size(), "В списке должен остаться 1 юзер");
        userStorage.deleteUser(3);
        assertEquals(0, userStorage.getAll().size(), "Список юзеров должен стать пустым");
    }
}