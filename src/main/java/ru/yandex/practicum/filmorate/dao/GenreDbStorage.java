package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@Slf4j
public class GenreDbStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;

    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Map<Integer, Genre> getAllGenres() {
        Map<Integer, Genre> allGenre = new HashMap<>();
        String querySql = "SELECT * FROM GENRE;";
        List<Genre> genreFromDb = jdbcTemplate.query(querySql, this::rowToGenre);
        for (Genre genre : genreFromDb) {
            allGenre.put(genre.getId(), genre);
        }
        return allGenre;
    }

    @Override
    public Optional<Genre> getGenreById(Integer id) {
        String querySql = "SELECT * FROM GENRE WHERE GENRE_ID = ?";
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet(querySql, id);
        if (genreRows.next()) {
            Genre genre = new Genre(genreRows.getInt("GENRE_ID"),
                    genreRows.getString("GENRE_NAME"));
            log.info("Найден жанр с id {}", id);
            return Optional.of(genre);
        }
        log.warn("Жанр с id {} не найден", id);
        return Optional.empty();
    }

    private Genre rowToGenre(ResultSet rs, int rowNum) throws SQLException {
        return new Genre(rs.getInt("GENRE_ID"), rs.getString("GENRE_NAME"));
    }
}
