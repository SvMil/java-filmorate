package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;

import java.time.LocalDate;
import java.util.*;


@Component("inMemoryFilmStorage")
public class InMemoryFilmStorage implements FilmStorage {

    private final UserStorage userStorage;

    @Autowired
    public InMemoryFilmStorage(@Qualifier("inMemoryUserStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    private final Map<Long, Film> films = new HashMap<>();

    @Override
    public Map<Long, Film> getFilms() {
        return films;
    }

    @Override
    public Film create(Film film) {
        validateFilm(film);
        film.setId(getNextId());
        films.put(film.getId(), film);
        return film;
    }

    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    private void validateFilm(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            throw new ConditionsNotMetException("Название не может быть пустым");
        }
        if (film.getDescription().length() > 200) {
            throw new ConditionsNotMetException("Описание не может быть длинее 200 символов");
        }
        if (film.getDuration() <= 0) {
            throw new ConditionsNotMetException("Продолжительность фильма должна быть положительным числом.");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895,12,28))) {
            throw new ConditionsNotMetException("Дата релиза должна быть не раньше 28 декабря 1895 года");
        }
    }

    @Override
    public Film update(Film newFilm) {
            Film oldFilm = films.get(newFilm.getId());
            validateFilm(newFilm);
            oldFilm.setName(newFilm.getName());
            oldFilm.setDescription(newFilm.getDescription());
            oldFilm.setDuration(newFilm.getDuration());
            oldFilm.setReleaseDate(newFilm.getReleaseDate());
            return oldFilm;
    }

    @Override
    public Film getFilmById(long id) {
        if (films.containsKey(id)) {
            return films.get(id);
        }
        return null;
    }

    @Override
    public void addLike(long filmId, long userId) {
        Film film = getFilmById(filmId);
        if (film != null && userStorage.getUserById(userId) != null) {
            getFilmById(filmId).getLikes().add(userId);
        }
    }

    @Override
    public void deleteLike(long filmId, long userId) {
        Film film = getFilmById(filmId);
        if (film != null && userStorage.getUserById(userId) != null) {
            film.getLikes().remove(userId);
        }
    }

    @Override
    public void deleteFilm(long filmId) {
        films.remove(filmId);
    }
}