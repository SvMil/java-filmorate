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
        System.out.println("id переданный в сервис " + id);
        System.out.println("значение genreStorage.getGenreById(id) сервис ");
        System.out.println(genreStorage.getGenreById(id));
        System.out.println("окончание значения genreStorage.getGenreById(id) сервис ");

        if (genreStorage.getGenreById(id) == null) {
            log.warn("Значение жанра с id " + id +  " не найдено");
            throw new NotFoundException("Значение жанра с id " + id +  " не найдено");
        }
        return genreStorage.getGenreById(id);
    }
}
