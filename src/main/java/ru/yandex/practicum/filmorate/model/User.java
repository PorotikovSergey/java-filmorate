package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import java.util.Date;

@Data
public class User {
    private int id;
    private String email;
    private String login;
    private String name;
    private Date birthday;

    public User (int id, String email, String login, String name, Date birthday) {
        this.id=id;
        this.email=email;
        this.login=login;
        this.name=name;
        this.birthday=birthday;
    }
}
