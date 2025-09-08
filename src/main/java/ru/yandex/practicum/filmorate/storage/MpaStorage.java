package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Optional;
import java.util.Map;

@Component("MpaStorage")
public interface MpaStorage {
        Optional<Mpa> getMpaById(Integer id);
        Map<Integer, Mpa> getAllMpa();

    }

