package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

public class UsersControllerTest {
    UserController userController = new UserController();

    @Test
    public void createFilm() {

        User user = User.builder()
                .name("Александр")
                .login("alex86")
                .email("al86mail@mail.ru")
                .birthday(LocalDate.of(2024, 10, 3))
                .build();
        userController.create(user);
        System.out.println(userController.findAll());
    }


    @Test
    public void updateFilm() {

        User user = User.builder()
                .name("Александр")
                .login("alex86")
                .email("al86mail@mail.ru")
                .birthday(LocalDate.of(1986, 10, 3))
                .build();
        userController.create(user);

        System.out.println(user.toString());

        User userUpdate = User.builder()
                .id(user.getId())
                .name("Александр Евгеньевич")
                .login("alex86ya")
                .email("al8610ya@ya.ru")
                .birthday(LocalDate.of(1986, 10, 3))
                .build();
        userController.update(userUpdate);

        System.out.println(userController.findAll());
    }


}