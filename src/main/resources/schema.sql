create table IF NOT EXISTS RATING
(
    RATING_ID   int auto_increment,
    RATING_NAME varchar(5),
    constraint RATING_PK
        primary key (rating_id)
);

create table IF NOT EXISTS GENRE
(
    GENRE_ID   INTEGER auto_increment,
    GENRE_NAME CHARACTER VARYING(15) not null,
    constraint GENRE_PK
        primary key (GENRE_ID)
);

create table IF NOT EXISTS FILMS
(
    FILM_ID          INTEGER auto_increment,
    FILM_NAME        CHARACTER VARYING(50) not null,
    FILM_DESCRIPTION CHARACTER VARYING(500),
    FILM_DURATION    BIGINT                not null,
    FILM_RELEASEDATE DATE,
    FILM_RATING INTEGER not null,
    constraint FILM_RATING_PK
        foreign key (FILM_RATING) references RATING(RATING_ID),
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

create table IF NOT EXISTS USERS
(
    USER_ID       INTEGER auto_increment,
    USER_NAME     CHARACTER VARYING(20) not null,
    USER_LOGIN    CHARACTER VARYING(20) not null,
    USER_EMAIL    CHARACTER VARYING(20) not null,
    USER_BIRTHDAY DATE                  not null,
    constraint USERS_PK
        primary key (USER_ID)
);

create table IF NOT EXISTS FRIENDSHIP
(
    USER_ID   INTEGER               not null,
    FRIEND_ID INTEGER               not null,
    STATUS    CHARACTER VARYING(15) not null,
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

create unique index IF NOT EXISTS USERS_USER_EMAIL_UINDEX
    on USERS (USER_EMAIL);

create unique index IF NOT EXISTS USERS_USER_LOGIN_UINDEX
    on USERS (USER_LOGIN);