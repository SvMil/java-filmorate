package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Map;

public interface FilmStorage {

    Film create(Film film);

    Film update(Film film);

    void deleteFilm(long filmId);

    Film getFilmById(long id);

    Map<Long, Film> getFilms();

    void addLike(long filmId, long userId);

    void deleteLike(long filmId, long userId);
}