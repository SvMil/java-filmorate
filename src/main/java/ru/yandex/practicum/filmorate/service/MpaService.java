package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.MpaDbStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;
import java.util.Collections;

@Service
@Slf4j
@RequiredArgsConstructor
public class MpaService {
    private final MpaDbStorage mpaStorage;

    public Mpa getMpaById(Integer id) {
        if (mpaStorage.getMpaById(id) == null) {
            log.warn("Значение рейтинга с id {} не найдено", id);
            throw new NotFoundException("Значение рейтинга не найдено");
        }
        System.out.println("печать из MpaService");
        System.out.println(mpaStorage.getMpaById(id));
        System.out.println("печать имени рейтинга из MpaService");
        System.out.println(mpaStorage.getMpaById(id).getName());
        System.out.println("конец печати из MpaService");
        return mpaStorage.getMpaById(id);
    }

    public Collection<Mpa> getAllMpa() {
        log.info("Получение списка значений рейтинга");
        return Collections.unmodifiableCollection(mpaStorage.getAllMpa().values());
    }
}