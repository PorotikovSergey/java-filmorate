package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    UserController controller = new UserController();

    @Test
    void createUsersRight() throws ValidationException {
        User user = new User();
        user.setName("Вася");
        user.setId(1);
        user.setLogin("Нагибатор3000");
        user.setEmail("nagibator@mail.ru");
        user.setBirthday(LocalDate.of(1999, 6, 26));
        controller.create(user);
        assertEquals("Вася", user.getName(), "Именя не совпадает");
        assertEquals("Нагибатор3000", user.getLogin(), "Логины не совпадает");
        assertEquals("nagibator@mail.ru", user.getEmail(), "Длительность не совпадает");
        assertEquals(1, user.getId(), "Id не совпадает");
        assertEquals("1999-06-26", user.getBirthday().toString(), "День рожденья не совпадает");

        User user2 = new User();
        user2.setName("");
        user2.setId(2);
        user2.setLogin("Нагибатор3001");
        user2.setEmail("nagibator@mail.com");
        user2.setBirthday(LocalDate.of(1999, 10, 26));
        controller.create(user2);
        assertEquals("Нагибатор3001", user2.getName(), "Имя должно было сменить на логин");
    }

}