package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class MpaDbStorage implements MpaStorage {

    private final JdbcTemplate jdbcTemplate;

    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Map<Integer, Mpa> getAllMpa() {
        Map<Integer, Mpa> allMpa = new HashMap<>();
        String sqlQuery = "SELECT * FROM MPA;";
        List<Mpa> mpaFromDb = jdbcTemplate.query(sqlQuery, this::rowToMpa);
        for (Mpa mpa : mpaFromDb) {
            allMpa.put(mpa.getId(), mpa);
        }
        return allMpa;
    }

    @Override
    public Mpa getMpaById(Integer id) {
        String sqlQuery = "SELECT * FROM MPA WHERE MPA_ID = ?";
        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet(sqlQuery, id);
        if (mpaRows.next()) {
            Mpa mpa = new Mpa(mpaRows.getInt("MPA_ID"), mpaRows.getString("RATING"));
            log.info("Pейтинг с id {} найден", id);
            return mpa;
        }
        log.warn("Рейтинг с id {} не найден", id);
        return null;
    }

    private Mpa rowToMpa(ResultSet rs, int rowNum) throws SQLException {
        return new Mpa(rs.getInt("MPA_ID"), rs.getString("RATING"));
    }
}
