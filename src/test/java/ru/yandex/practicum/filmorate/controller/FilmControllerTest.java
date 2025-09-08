package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;


import java.time.LocalDate;

public class FilmControllerTest {

    UserStorage userStorage = new InMemoryUserStorage();
    FilmStorage filmStorage = new InMemoryFilmStorage(userStorage);
    FilmService filmService = new FilmService(filmStorage, userStorage);
    FilmController filmController = new FilmController(filmService);

    UserService userService = new UserService(userStorage);
    UserController userController = new UserController(userService);

    @Test
    public void createFilm() {

        Film film = Film.builder()
                .name("Анора")
                .description("Американская драма")
                .duration(139)
                .releaseDate(LocalDate.of(2024, 10, 3))
                .build();
        filmController.create(film);
        System.out.println(filmController.getAllFilms());
        System.out.println(filmController.getFilmById(film.getId()));
    }

    @Test
    public void updateFilm() {

        Film film = Film.builder()
                .name("Анора")
                .description("Американская драма")
                .duration(139)
                .releaseDate(LocalDate.of(2024, 10, 3))
                .build();
        filmController.create(film);

        System.out.println(film.toString());

        Film filmUpdate = Film.builder()
                .id(film.getId())
                .name("Области тьмы")
                .description("Американский триллер")
                .duration(105)
                .releaseDate(LocalDate.of(2011, 12, 11))
                .build();
        filmController.update(filmUpdate);

        System.out.println(filmController.getAllFilms());
    }

    @Test
    public void likeFilm() {

        User user = User.builder()
                .name("Александр")
                .login("alex86")
                .email("al86mail@mail.ru")
                .birthday(LocalDate.of(1986, 10, 3))
                .build();
        userController.create(user);


        User user2 = User.builder()
                .name("Иван")
                .login("iva87")
                .email("iv87mail@mail.ru")
                .birthday(LocalDate.of(1987, 11, 9))
                .build();
        userController.create(user2);

        User user3 = User.builder()
                .name("Кирилл")
                .login("kir85")
                .email("kir85mail@mail.ru")
                .birthday(LocalDate.of(1985, 12, 7))
                .build();
        userController.create(user3);

        User user4 = User.builder()
                .name("Андрей")
                .login("an85")
                .email("andy85mail@mail.ru")
                .birthday(LocalDate.of(1985, 10, 17))
                .build();
        userController.create(user4);

        Film film = Film.builder()
                .name("Анора")
                .description("Американская драма")
                .duration(139)
                .releaseDate(LocalDate.of(2024, 10, 3))
                .build();
        filmController.create(film);

        System.out.println(film.toString());

        Film film2 = Film.builder()
                .name("Области тьмы")
                .description("Американский триллер")
                .duration(105)
                .releaseDate(LocalDate.of(2011, 12, 11))
                .build();
        filmController.create(film2);

        Film film3 = Film.builder()
                .name("Бегущий по лезвию")
                .description("Драма США")
                .duration(164)
                .releaseDate(LocalDate.of(2017, 9, 15))
                .build();
        filmController.create(film3);

        Film film4 = Film.builder()
                .name("Гаттака")
                .description("Детектив США")
                .duration(106)
                .releaseDate(LocalDate.of(1997, 11, 5))
                .build();
        filmController.create(film4);

        filmController.addLike(film.getId(),user.getId());
        filmController.addLike(film.getId(),user2.getId());
        filmController.addLike(film2.getId(),user.getId());
        filmController.addLike(film4.getId(),user.getId());
        filmController.addLike(film4.getId(),user2.getId());
        filmController.addLike(film4.getId(),user3.getId());
        filmController.addLike(film3.getId(),user.getId());
        filmController.addLike(film3.getId(),user2.getId());
        filmController.addLike(film3.getId(),user3.getId());
        filmController.addLike(film3.getId(),user4.getId());
        filmController.getTopFilms(4);

        System.out.println(filmController.getTopFilms(4));
    }
}