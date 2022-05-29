package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.IdGenerator;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    UserController controller = new UserController();

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
        User user2 = new User();
        user2.setName("");
        user2.setLogin("Нагибатор3001");
        user2.setEmail("nagibator@mail.com");
        user2.setBirthday(LocalDate.of(1998, 10, 26));
        controller.create(user2);
        assertEquals("Нагибатор3001", user2.getName(), "Пустое имя должно было смениться на логин");
        assertEquals("Нагибатор3001", user2.getLogin(), "Логин должен был остаться прежним");
    }

    @Test
    void doNotCreateUserWithWrongEmail() {
        User user3 = new User();
        user3.setName("Петя");
        user3.setLogin("Мамколюб1997");
        user3.setEmail("motherloverAmail.com");
        user3.setBirthday(LocalDate.of(1997, 10, 26));
        Throwable thrown = assertThrows(ValidationException.class, () -> {
            controller.create(user3);
        });
        assertNotNull(thrown.getMessage(), "Нельзя добавить юзера с неправильным email: " + user3.getEmail());
    }

    @Test
    void doNotCreateUserFromFuture() {
        User user4 = new User();
        user4.setName("Алёша");
        user4.setLogin("AlEx");
        user4.setEmail("alexey@gmail.com");
        user4.setBirthday(LocalDate.of(3000, 10, 26));
        Throwable thrown2 = assertThrows(ValidationException.class, () -> {
            controller.create(user4);
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

        User user2 = new User();
        user2.setId(user.getId());
        user2.setName("Петя");
        user2.setLogin("Нагибатор3333");
        user2.setEmail("nagibator3333@mail.com");
        user2.setBirthday(LocalDate.of(2000, 10, 21));
        controller.refresh(user2);
        assertFalse(controller.findAll().contains(user), "Юзера не должно было остаться");
        assertTrue(controller.findAll().contains(user2), "Юзер2 не должен был заменить юзера");
        assertEquals(user.getId(), user2.getId(), "Id у юзера2 должен был стать у первого юзера раньше");
    }

    @Test
    void refreshUserWithNewIdRight() throws ValidationException {
        User user = new User();
        user.setName("Вася");
        user.setLogin("Нагибатор3000");
        user.setEmail("nagibator@mail.ru");
        user.setBirthday(LocalDate.of(1999, 6, 26));
        controller.create(user);

        User user3 = new User();
        user3.setName("Миша");
        user3.setLogin("Нагибатор1");
        user3.setEmail("nagibator1@mail.com");
        user3.setBirthday(LocalDate.of(2001, 11, 11));
        controller.refresh(user3);

        assertTrue(controller.findAll().contains(user), "Юзера должен был остаться");
        assertTrue(controller.findAll().contains(user3), "Юзер3 должен был просто добавиться");
    }

    @Test
    void refreshUserWithEmptyName() throws ValidationException {
        User user = new User();
        user.setName("Вася");
        user.setLogin("Нагибатор3000");
        user.setEmail("nagibator@mail.ru");
        user.setBirthday(LocalDate.of(1999, 6, 26));
        controller.create(user);

        User user4 = new User();
        user4.setId(user.getId());
        user4.setName("");
        user4.setLogin("Нагибаторщик");
        user4.setEmail("nagibator@69mail.com");
        user4.setBirthday(LocalDate.of(2001, 11, 11));
        controller.refresh(user4);

        assertEquals("Нагибаторщик", user4.getName(), "Пустое имя должно было смениться на логин");
        assertFalse(controller.findAll().contains(user), "Юзер должен был удалиться");
        assertTrue(controller.findAll().contains(user4), "Юзер4 должен был просто добавиться вместо первого");
    }

    @Test
    void refreshUserWithWrongEmail() throws ValidationException {
        User user = new User();
        user.setName("Вася");
        user.setLogin("Нагибатор3000");
        user.setEmail("nagibator@mail.ru");
        user.setBirthday(LocalDate.of(1999, 6, 26));
        controller.create(user);

        User user5 = new User();
        user5.setName("Володя");
        user5.setLogin("Нагибаторщик");
        user5.setEmail("nagibator69mail.com");
        user5.setBirthday(LocalDate.of(2001, 11, 11));
        Throwable thrown = assertThrows(ValidationException.class, () -> {
            controller.create(user5);
        });

        assertNotNull(thrown.getMessage(), "Нельзя обновить юзера с неправильной почтой");
        assertFalse(controller.findAll().contains(user5), "Юзер5 не должен был просто добавиться");
    }

    @Test
    void returnRightSize() throws ValidationException {
        User user = new User();
        user.setName("Вася");
        user.setLogin("Нагибатор3000");
        user.setEmail("nagibator@mail.ru");
        user.setBirthday(LocalDate.of(1999, 6, 26));
        controller.create(user);

        User user2 = new User();
        user2.setName("Петя");
        user2.setLogin("Нагибатор2000");
        user2.setEmail("nagibator2000@mail.ru");
        user2.setBirthday(LocalDate.of(1998, 6, 26));
        controller.create(user2);

        User user3 = new User();
        user3.setName("");
        user3.setLogin("Нагибатор3000");
        user3.setEmail("nagibator1000@mail.ru");
        user3.setBirthday(LocalDate.of(1997, 6, 26));
        controller.create(user3);

        assertEquals(3, controller.findAll().size(), "В списке должно быть 3 юзера");
    }
}