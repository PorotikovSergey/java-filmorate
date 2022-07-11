-- DROP TABLE FRIENDSHIP;
-- DROP TABLE FILM_GENRE_ACCORDING;
-- DROP TABLE FILM_MPA_ACCORDING;
-- DROP TABLE GENRE;
-- DROP TABLE LIKED_FILMS;
-- DROP TABLE FILMS;
-- DROP TABLE USERS;
-- DROP TABLE MPA;

create table IF NOT EXISTS GENRE
(
    GENRE_ID   INTEGER NOT NULL,
    GENRE_NAME CHARACTER VARYING(15),
    constraint GENRE_PK
        primary key (GENRE_ID)
);

INSERT INTO GENRE (GENRE_ID, GENRE_NAME)
VALUES (1, 'Комедия');
INSERT INTO GENRE (GENRE_ID, GENRE_NAME)
VALUES (2, 'Драма');
INSERT INTO GENRE (GENRE_ID, GENRE_NAME)
VALUES (3, 'Мультфильм');
INSERT INTO GENRE (GENRE_ID, GENRE_NAME)
VALUES (4, 'Триллер');
INSERT INTO GENRE (GENRE_ID, GENRE_NAME)
VALUES (5, 'Документальный');
INSERT INTO GENRE (GENRE_ID, GENRE_NAME)
VALUES (6, 'Боевик');

create table IF NOT EXISTS MPA
(
    MPA_ID   int NOT NULL,
    MPA_NAME varchar(5),
    constraint MPA_PK
        primary key (MPA_ID)
);

INSERT INTO MPA (MPA_ID, MPA_NAME)
VALUES (1, 'G');
INSERT INTO MPA (MPA_ID, MPA_NAME)
VALUES (2, 'PG');
INSERT INTO MPA (MPA_ID, MPA_NAME)
VALUES (3, 'PG-13');
INSERT INTO MPA (MPA_ID, MPA_NAME)
VALUES (4, 'R');
INSERT INTO MPA (MPA_ID, MPA_NAME)
VALUES (5, 'NC-17');

create table IF NOT EXISTS FILMS
(
    FILM_ID          INTEGER auto_increment UNIQUE,
    FILM_NAME        CHARACTER VARYING(50),
    FILM_DESCRIPTION CHARACTER VARYING(500),
    FILM_DURATION    INTEGER,
    FILM_RELEASEDATE DATE,
    FILM_RATING      INTEGER,
    constraint FILMS_PK
        primary key (FILM_ID)
);

create table IF NOT EXISTS FILM_GENRE_ACCORDING
(
    FILM_ID  INTEGER not null,
    GENRE_ID INTEGER not null,
    constraint FILM_ID_GENRE
        foreign key (FILM_ID) references FILMS,
    constraint GENRE_ID
        foreign key (GENRE_ID) references GENRE
);

create table FILM_MPA_ACCORDING
(
    FILM_ID INTEGER not null,
    MPA_ID  INTEGER not null,
    constraint FILM_MPA_ACCORDING_FILMS__FK
        foreign key (FILM_ID) references FILMS (FILM_ID),
    constraint FILM_MPA_ACCORDING_MPA_MPA_ID_FK
        foreign key (MPA_ID) references MPA
);

create table IF NOT EXISTS USERS
(
    USER_ID       INTEGER auto_increment UNIQUE,
    USER_NAME     CHARACTER VARYING(20),
    USER_LOGIN    CHARACTER VARYING(20),
    USER_EMAIL    CHARACTER VARYING(20),
    USER_BIRTHDAY DATE,
    constraint USERS_PK
        primary key (USER_ID)
);

create table IF NOT EXISTS FRIENDSHIP
(
    USER_ID   INTEGER not null,
    FRIEND_ID INTEGER not null,
    STATUS    BOOLEAN not null,
    constraint FRIEND_ID
        foreign key (FRIEND_ID) references USERS,
    constraint USER_ID
        foreign key (USER_ID) references USERS
);

create table IF NOT EXISTS LIKED_FILMS
(
    USER_ID INTEGER not null,
    FILM_ID INTEGER not null,
    constraint FILM_ID_LIKES
        foreign key (FILM_ID) references FILMS,
    constraint USER_ID_LIKES
        foreign key (USER_ID) references USERS
);
