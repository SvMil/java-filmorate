package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.Collection;
import java.util.Collections;

@Service
@Slf4j
public class MpaService {
    private final MpaStorage mpaStorage;

    @Autowired
    public MpaService(MpaStorage mpaStorage) {
        this.mpaStorage = mpaStorage;
    }

    public Mpa getMpaById(int id) {
        if (mpaStorage.getMpaById(id) == null) {
            log.warn("Значение рейтинга с id {} не найдено", id);
            throw new ConditionsNotMetException("Значение рейтинга не найдено");
        }
        return mpaStorage.getMpaById(id).orElseThrow();
    }

    public Collection<Mpa> getAllMpa() {
        log.info("Получение списка значений рейтинга");
        return Collections.unmodifiableCollection(mpaStorage.getAllMpa().values());
    }
}