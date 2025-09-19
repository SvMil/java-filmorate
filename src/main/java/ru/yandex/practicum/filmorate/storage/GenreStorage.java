package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Map;

@Component("GenreStorage")
public interface GenreStorage {

    Genre getGenreById(Integer id);

    Map<Integer, Genre> getAllGenres();

}