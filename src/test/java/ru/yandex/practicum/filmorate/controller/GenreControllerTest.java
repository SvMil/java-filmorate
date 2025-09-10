package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GenreControllerTest {

    private final GenreService genreService;
    private final GenreController genreController;

    @Test
    public void getAllGenres() {
        Collection<Genre> genres = genreService.getAllGenres();

        assertEquals(6, genres.size());
    }

    @Test
    public void findGenreById() {
        Genre genre = genreService.getGenreById(4);

        assertEquals("Триллер", genre.getName());
    }

    @Test
    public void findGenreByIdController() {
        Genre genre = genreController.getGenreById(4 );

        assertEquals("Триллер", genre.getName());
    }
}
