package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Collection;
import java.util.Collections;

@Slf4j
@Service
public class FilmService {

    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Film create(Film film) {
        log.info("Добавлен новый фильм с id "+ film.getId());
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        if (filmStorage.getFilmById(film.getId()) == null) {
            log.warn("Невозможно обновить фильм");
            throw new NotFoundException("Фильм не найден");
        }
        log.info("Фильм с id " + film.getId() + " был обновлён");
        return filmStorage.update(film);
    }

    public void deleteFilm(long filmId) {
        filmStorage.deleteFilm(filmId);
        log.info("Фильм с id "+ filmId + " был удалён");

    }

    public Collection<Film> getFilms() {
        log.info("Получение списка фильмов");
        return Collections.unmodifiableCollection(filmStorage.getFilms().values());
    }

    public Film getFilmById(long id) {
        Film film = filmStorage.getFilmById(id);
        if (film == null) {
            throw new NotFoundException("Фильм не найден");
        }
        log.info("Получение информации о фильме с id " + id);
        return film;
    }

    public void addLike(long filmId, long userId) {
        filmStorage.addLike(filmId, userId);
        log.info("Пользователь с id {} поставил фильму с id {} лайк", userId, filmId);
    }

    public void deleteLike(long filmId, long userId) {
        filmStorage.deleteLike(filmId, userId);
        log.info("Лайк пользователя с id {} фильму с id {} удалён", userId, filmId);
    }

    public List<Film> getTopFilms(int count) {
        log.info("Запрос топа {} самых популярных фильмов", count);
        return filmStorage.getFilms().values().stream()
                .sorted((f1, f2) -> f2.getLikes().size() - f1.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
    }
}