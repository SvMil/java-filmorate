package ru.yandex.practicum.filmorate.controller;


import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.service.MpaService;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MpaControllerTest {

    private final MpaService mpaService;

    @Test
    public void getAllMpa() {
        Collection<Mpa> allMpa = mpaService.getAllMpa();

        assertEquals(5, allMpa.size());
    }

    @Test
    public void findMpaById() {
        Mpa mpa = mpaService.getMpaById(1);

        assertEquals("G", mpa.getName());
    }
}