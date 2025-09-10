package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;


import java.util.*;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;


@Slf4j
@Component("filmDbStorage")
@Primary
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final UserStorage userStorage;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate, @Qualifier("userDbStorage") UserStorage userStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.userStorage = userStorage;
    }


    @Override
    public Film create(Film film) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String querySql = "INSERT INTO FILM (NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPA_ID) " +
                "VALUES (?, ?, ?, ?, ?);";
        String queryForFilmGenre = "INSERT INTO FILM_GENRE (FILM_ID, GENRE_ID) VALUES (?, ?);";
        jdbcTemplate.update(connection -> {
            PreparedStatement ps =
                    connection.prepareStatement(querySql, new String[]{"film_id"});
            ps.setString(1, film.getName());
            ps.setString(2, film.getDescription());
            ps.setDate(3, Date.valueOf(film.getReleaseDate()));
            ps.setLong(4, film.getDuration());
            ps.setInt(5, film.getMpa().getId());
            return ps;
        }, keyHolder);
        film.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());

        if (!film.getGenres().isEmpty()) {
            Set<Genre> sortGenres = film.getGenres();
            sortGenres.stream().sorted();
            for (Genre genre : sortGenres) {
                jdbcTemplate.update(queryForFilmGenre, film.getId(), genre.getId());
            }
        }
        return getFilmById(film.getId());
    }

    @Override
    public Film update(Film film) {
        String querySql = "UPDATE FILM SET NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ?, MPA_ID = ?, DURATION = ?" +
                " WHERE FILM_ID = ?;";
        String queryToDeleteFilmGenres = "DELETE FROM FILM_GENRE WHERE FILM_ID = ?;";
        String queryForUpdateGenre = "INSERT INTO FILM_GENRE (FILM_ID, GENRE_ID) VALUES (?, ?);";

        jdbcTemplate.update(querySql, film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getMpa().getId(), film.getDuration(), film.getId());
        jdbcTemplate.update(queryToDeleteFilmGenres, film.getId());
        if (!film.getGenres().isEmpty()) {
            for (Genre genre : film.getGenres()) {
                jdbcTemplate.update(queryForUpdateGenre, film.getId(), genre.getId());
            }
        }
        return getFilmById(film.getId());
    }


    public void deleteFilm(long filmId) {
        Film film = getFilmById(filmId);
        String querySql = "DELETE FROM FILM WHERE FILM_ID = ?;";
        jdbcTemplate.update(querySql, filmId);
    }

    @Override
    public Film getFilmById(long id) {
        String querySql = "SELECT F.*, M.RATING FROM FILM AS F JOIN MPA AS M ON F.MPA_ID = M.MPA_ID " +
                " WHERE FILM_ID = ?;";
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(querySql, id);
        if (filmRows.next()) {
            Film film = Film.builder()
                    .id(filmRows.getLong("FILM_ID"))
                    .name(filmRows.getString("NAME"))
                    .description(filmRows.getString("DESCRIPTION"))
                    .releaseDate(Objects.requireNonNull(filmRows.getDate("RELEASE_DATE")).toLocalDate())
                    .duration(filmRows.getInt("DURATION"))
                    .mpa(new Mpa(filmRows.getInt("MPA_ID"), filmRows.getString("RATING")))
                    .build();
            List<Genre> genresOfFilm = getGenresOfFilm(id);
            List<Integer> likes = getLikesOfFilm(film.getId());
            for (Genre genre : genresOfFilm) {
                film.getGenres().add(genre);
            }
            for (Integer like : likes) {
                film.getLikes().add(Long.valueOf(like));
            }
            log.info("Найден фильм с id {}", id);
            return film;
        }
        log.warn("Фильм с id {} не найден", id);
        throw new NotFoundException("Фильм не найден");
    }

    @Override
    public Map<Long, Film> getFilms() {
        Map<Long, Film> films = new HashMap<>();
        String querySql = "SELECT F.*, M.RATING FROM FILM AS F JOIN MPA AS M ON F.MPA_ID = M.MPA_ID ";
        List<Film> filmsFromDb = jdbcTemplate.query(querySql, this::rowToFilm);
        for (Film film : filmsFromDb) {
            films.put(film.getId(), film);
        }
        return films;
    }

    private Film rowToFilm(ResultSet rs, int rowNum) throws SQLException {
        log.info("Film build start>>>>>");
        Film film = Film.builder()
                .id(rs.getLong("FILM_ID"))
                .name(rs.getString("NAME"))
                .description(rs.getString("DESCRIPTION"))
                .releaseDate(rs.getDate("RELEASE_DATE").toLocalDate())
                .duration(rs.getInt("DURATION"))
                .mpa(new Mpa(rs.getInt("MPA_ID"), rs.getString("RATING")))
                .build();
        log.info("Film = {}", film);
        List<Genre> genresOfFilm = getGenresOfFilm(film.getId());
        List<Integer> likes = getLikesOfFilm(film.getId());
        for (Genre genre : genresOfFilm) {
            film.getGenres().add(genre);
        }
        for (Integer like : likes) {
            film.getLikes().add(Long.valueOf(like));
        }
        return film;
    }

    private Genre mapRowToGenre(ResultSet rs, int rowNum) throws SQLException {
        return new Genre(rs.getInt("GENRE_ID"), rs.getString("GENRE_NAME"));
    }

    private Integer mapRowToLike(ResultSet rs, int rowNum) throws SQLException {
        return rs.getInt("USER_ID");
    }

    private List<Genre> getGenresOfFilm(long filmId) {
        String queryForFilmGenres = "SELECT FG.FILM_ID, FG.GENRE_ID, G.GENRE_NAME FROM FILM_GENRE FG" +
                " JOIN GENRE G ON G.GENRE_ID = FG.GENRE_ID WHERE FILM_ID = ?;";
        return jdbcTemplate.query(queryForFilmGenres, this::mapRowToGenre, filmId);
    }

    public List<Integer> getLikesOfFilm(long filmId) {
        String queryForFilmLikes = "SELECT USER_ID FROM FILM_LIKE WHERE FILM_ID = ?;";
        return jdbcTemplate.query(queryForFilmLikes, this::mapRowToLike, filmId);
    }

    @Override
    public void addLike(long filmId, long userId) {
        Film film = getFilmById(filmId);
        try {
            String querySql = "INSERT INTO FILM_LIKE (FILM_ID, USER_ID) VALUES (?, ?);";
            jdbcTemplate.update(querySql, filmId, userId);
        } catch (Exception e) {
            log.warn("Лайк фильму с id {} от пользователя с id {} уже существует", filmId, userId);
        }
    }

    @Override
    public void deleteLike(long filmId, long userId) {
        Film film = getFilmById(filmId);
        try {
        String querySql = "DELETE FROM FILM_LIKE WHERE FILM_ID = ? AND USER_ID = ?;";
        jdbcTemplate.update(querySql, filmId, userId);
        } catch (NotFoundException e) {

            log.warn("Лайка фильму с id {} от пользователя с id {} не существует", filmId, userId);

        }
    }


    public void deleteLike2(long filmId, long userId) {
        Film film = getFilmById(filmId);
        try {
            String querySql = "DELETE FROM FILM_LIKE WHERE FILM_ID = ? AND USER_ID = ?;";
            jdbcTemplate.update(querySql, filmId, userId);
        } catch (NotFoundException e) {

            log.warn("Лайка фильму с id {} от пользователя с id {} не существует", filmId, userId);

        }
    }
}