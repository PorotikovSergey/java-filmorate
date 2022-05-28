package ru.yandex.practicum.filmorate.controller.exceptions;

public class ValidationException extends Throwable {
    public ValidationException(String s) {
        super(s);
    }
}
