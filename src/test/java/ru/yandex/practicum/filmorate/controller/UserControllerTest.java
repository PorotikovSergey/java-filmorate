package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.IdGenerator;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    private final UserController controller = new UserController();

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
        controller.create(user);
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
        controller.create(user);
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
            controller.create(user);
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
            controller.create(user);
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
        controller.create(user);

        User newUser = new User();
        newUser.setId(user.getId());
        newUser.setName("Петя");
        newUser.setLogin("Нагибатор3333");
        newUser.setEmail("nagibator3333@mail.com");
        newUser.setBirthday(LocalDate.of(2000, 10, 21));
        controller.refresh(newUser);
        assertFalse(controller.findAll().contains(user), "Юзера не должно было остаться");
        assertTrue(controller.findAll().contains(newUser), "Юзер2 не должен был заменить юзера");
        assertEquals(user.getId(), newUser.getId(), "Id у юзера2 должен был стать у первого юзера раньше");
    }

    @Test
    void refreshUserWithNewIdRight() throws ValidationException {
        User user = new User();
        user.setName("Вася");
        user.setLogin("Нагибатор3000");
        user.setEmail("nagibator@mail.ru");
        user.setBirthday(LocalDate.of(1999, 6, 26));
        controller.create(user);

        User newUser = new User();
        newUser.setName("Миша");
        newUser.setLogin("Нагибатор1");
        newUser.setEmail("nagibator1@mail.com");
        newUser.setBirthday(LocalDate.of(2001, 11, 11));
        controller.refresh(newUser);

        assertTrue(controller.findAll().contains(user), "Юзера должен был остаться");
        assertTrue(controller.findAll().contains(newUser), "Юзер3 должен был просто добавиться");
    }

    @Test
    void refreshUserWithEmptyName() throws ValidationException {
        User user = new User();
        user.setName("Вася");
        user.setLogin("Нагибатор3000");
        user.setEmail("nagibator@mail.ru");
        user.setBirthday(LocalDate.of(1999, 6, 26));
        controller.create(user);

        User newUser = new User();
        newUser.setId(user.getId());
        newUser.setName("");
        newUser.setLogin("Нагибаторщик");
        newUser.setEmail("nagibator@69mail.com");
        newUser.setBirthday(LocalDate.of(2001, 11, 11));
        controller.refresh(newUser);

        assertEquals("Нагибаторщик", newUser.getName(), "Пустое имя должно было смениться на логин");
        assertFalse(controller.findAll().contains(user), "Юзер должен был удалиться");
        assertTrue(controller.findAll().contains(newUser), "Юзер4 должен был просто добавиться вместо первого");
    }

    @Test
    void refreshUserWithWrongEmail() throws ValidationException {
        User user = new User();
        user.setName("Вася");
        user.setLogin("Нагибатор3000");
        user.setEmail("nagibator@mail.ru");
        user.setBirthday(LocalDate.of(1999, 6, 26));
        controller.create(user);

        User newUser = new User();
        newUser.setName("Володя");
        newUser.setLogin("Нагибаторщик");
        newUser.setEmail("nagibator69mail.com");
        newUser.setBirthday(LocalDate.of(2001, 11, 11));
        Throwable thrown = assertThrows(ValidationException.class, () -> {
            controller.create(newUser);
        });

        assertNotNull(thrown.getMessage(), "Нельзя обновить юзера с неправильной почтой");
        assertFalse(controller.findAll().contains(newUser), "Юзер5 не должен был просто добавиться");
    }

    @Test
    void returnRightSize() throws ValidationException {
        User user = new User();
        user.setName("Вася");
        user.setLogin("Нагибатор3000");
        user.setEmail("nagibator@mail.ru");
        user.setBirthday(LocalDate.of(1999, 6, 26));
        controller.create(user);

        User newUser = new User();
        newUser.setName("Петя");
        newUser.setLogin("Нагибатор2000");
        newUser.setEmail("nagibator2000@mail.ru");
        newUser.setBirthday(LocalDate.of(1998, 6, 26));
        controller.create(newUser);

        User oneMoreUser = new User();
        oneMoreUser.setName("");
        oneMoreUser.setLogin("Нагибатор3000");
        oneMoreUser.setEmail("nagibator1000@mail.ru");
        oneMoreUser.setBirthday(LocalDate.of(1997, 6, 26));
        controller.create(oneMoreUser);

        assertEquals(3, controller.findAll().size(), "В списке должно быть 3 юзера");
    }
}