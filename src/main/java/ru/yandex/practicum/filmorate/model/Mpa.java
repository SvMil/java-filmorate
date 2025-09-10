package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class Mpa {
    public Integer id;
    public String name;

    public Mpa(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}
