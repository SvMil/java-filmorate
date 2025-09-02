package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;


import java.time.LocalDate;

public class FilmControllerTest {

    UserStorage userStorage = new InMemoryUserStorage();
    FilmStorage filmStorage = new InMemoryFilmStorage(userStorage);
    FilmService filmService = new FilmService(filmStorage);
    FilmController filmController = new FilmController(filmService);

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
}