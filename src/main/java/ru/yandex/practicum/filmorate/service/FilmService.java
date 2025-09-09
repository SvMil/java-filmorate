package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Collection;
import java.util.Collections;

@Slf4j
@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;

    }

    public Film create(Film film) {
        validateFilm(film);
        log.info("Добавлен новый фильм с id " + film.getId());
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        if (filmStorage.getFilmById(film.getId()) == null) {
            log.warn("Невозможно обновить фильм");
            throw new NotFoundException("Фильм не найден");
        }
        validateFilm(film);
        log.info("Фильм с id " + film.getId() + " был обновлён");
        return filmStorage.update(film);
    }

    public void deleteFilm(long filmId) {
        if (filmStorage.getFilmById(filmId) == null) {
            log.warn("Невозможно удалить фильм");
            throw new NotFoundException("Фильм не найден");
        }
        filmStorage.deleteFilm(filmId);
        log.info("Фильм с id " + filmId + " был удалён");

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
        if (filmStorage.getFilmById(filmId) == null) {
            throw new NotFoundException("Фильм не найден");
        }
        if (userStorage.getUserById(userId) == null) {
            throw new NotFoundException("Пользователь не найден");
        }
        filmStorage.addLike(filmId, userId);
        log.info("Пользователь с id {} поставил фильму с id {} лайк", userId, filmId);
    }

    public void deleteLike(long filmId, long userId) {
        if (filmStorage.getFilmById(filmId) == null) {
            throw new NotFoundException("Фильм не найден");
        }
        if (userStorage.getUserById(userId) == null) {
            throw new NotFoundException("Пользователь не найден");
        }
        if (!filmStorage.getFilmById(filmId).getLikes().contains(userId)) {
            throw new NotFoundException("Лайк пользователя не найден");
        }
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

    private void validateFilm(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            throw new ConditionsNotMetException("Название не может быть пустым");
        }
        if (film.getDescription().length() > 200) {
            throw new ConditionsNotMetException("Описание не может быть длинее 200 символов");
        }
        if (film.getMpa().getId() > 5) {
            throw new ConditionsNotMetException("Id рейтинга не может быть больше 5");
        }
        if (film.getDuration() <= 0) {
            throw new ConditionsNotMetException("Продолжительность фильма должна быть положительным числом.");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895,12,28))) {
            throw new ConditionsNotMetException("Дата релиза должна быть не раньше 28 декабря 1895 года");
        }
    }
}