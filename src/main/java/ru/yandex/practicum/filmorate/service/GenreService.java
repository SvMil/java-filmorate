package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.GenreDbStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.Collections;

@Service
@Slf4j
public class GenreService {
    private final GenreDbStorage genreStorage;

    @Autowired
    public GenreService(GenreDbStorage genreStorage) {
        this.genreStorage = genreStorage;
    }

    public Collection<Genre> getAllGenres() {
        return Collections.unmodifiableCollection(genreStorage.getAllGenres().values());
    }

    public Genre getGenreById(Integer id) {
        if (genreStorage.getGenreById(id) == null) {
            log.warn("Значение жанра с id {} не найдено", id);
            throw new NotFoundException("Значение рейтинга не найдено");
        }
        return genreStorage.getGenreById(id);
    }
}
