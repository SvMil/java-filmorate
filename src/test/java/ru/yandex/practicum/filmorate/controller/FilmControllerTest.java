package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;


import java.time.LocalDate;

public class FilmControllerTest {
    FilmController filmController = new FilmController();
    @Test
    public void createFilm() {

        Film film = Film.builder()
                .name("Анора")
                .description("Американская драма")
                .duration(139)
                .releaseDate(LocalDate.of(2024, 10, 3))
                .build();
        filmController.create(film);
        System.out.println(filmController.findAll());
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

        System.out.println(filmController.findAll());
    }


}