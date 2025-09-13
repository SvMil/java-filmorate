package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.dao.MpaDbStorage;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({MpaDbStorage.class})
public class JdbcMpaDbControllerTest {

    private final MpaDbStorage mpaDbStorage;

    @Test
    public void getAllMpa() {
        Map<Integer, Mpa> allMpa = mpaDbStorage.getAllMpa();

        assertEquals(5, allMpa.size());
    }

    @Test
    public void findMpaById() {
        Mpa mpa = mpaDbStorage.getMpaById(1);
        System.out.println(mpa.getName());

        assertEquals("G", mpa.getName());
    }
}