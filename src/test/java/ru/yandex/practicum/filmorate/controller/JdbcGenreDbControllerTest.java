package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.dao.GenreDbStorage;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({GenreDbStorage.class})
public class JdbcGenreDbControllerTest {

    private final GenreDbStorage genreDbStorage;

    @Test
    public void getAllGenres() {
        Map<Integer, Genre> genres = genreDbStorage.getAllGenres();

        assertEquals(6, genres.size());
    }

    @Test
    public void findGenreById() {
        Genre genre = genreDbStorage.getGenreById(4);

        assertEquals("Триллер", genre.getName());
    }

    @Test
    public void findGenreByIdController() {
        Genre genre = genreDbStorage.getGenreById(4);

        assertEquals("Триллер", genre.getName());
    }
}
