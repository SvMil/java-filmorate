package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.Collection;
import java.util.Collections;

@Service
@Slf4j
@RequiredArgsConstructor
public class MpaService {
    private final MpaStorage mpaStorage;

    public Mpa getMpaById(Integer id) {
        if (mpaStorage.getMpaById(id) == null) {
            log.warn("Значение рейтинга с id {} не найдено", id);
            throw new NotFoundException("Значение рейтинга не найдено");
        }
        return mpaStorage.getMpaById(id);
    }

    public Collection<Mpa> getAllMpa() {
        log.info("Получение списка значений рейтинга");
        return Collections.unmodifiableCollection(mpaStorage.getAllMpa().values());
    }
}