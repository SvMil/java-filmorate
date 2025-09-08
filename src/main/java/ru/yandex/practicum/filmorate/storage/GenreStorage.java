package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Map;
import java.util.Optional;

@Component("GenreStorage")
public interface GenreStorage {

    Optional<Genre> getGenreById(Integer id);
    Map<Integer, Genre> getAllGenres();

}