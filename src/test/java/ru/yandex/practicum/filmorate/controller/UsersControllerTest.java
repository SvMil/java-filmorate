package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;

public class UsersControllerTest {
    UserStorage userStorage = new InMemoryUserStorage();
    UserService userService = new UserService(userStorage);
    UserController userController = new UserController(userService);

    @Test
    public void createUser() {

        User user = User.builder()
                .name("Александр")
                .login("alex86")
                .email("al86mail@mail.ru")
                .birthday(LocalDate.of(2024, 10, 3))
                .build();
        userController.create(user);
        System.out.println(userController.getAllUsers());
        System.out.println(userController.getUserById(user.getId()));
    }

    @Test
    public void updateUser() {

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

        System.out.println(userController.getAllUsers());
    }

    @Test
    public void addFriend() {

        User user = User.builder()
                .name("Александр")
                .login("alex86")
                .email("al86mail@mail.ru")
                .birthday(LocalDate.of(1986, 10, 3))
                .build();
        userController.create(user);


        User user2 = User.builder()
                .name("Иван")
                .login("iva87")
                .email("iv87mail@mail.ru")
                .birthday(LocalDate.of(1987, 11, 9))
                .build();
        userController.create(user2);


        User user3 = User.builder()
                .name("Кирилл")
                .login("kir85")
                .email("kir85mail@mail.ru")
                .birthday(LocalDate.of(1985, 12, 7))
                .build();
        userController.create(user3);

        User user4 = User.builder()
                .name("Андрей")
                .login("an85")
                .email("andy85mail@mail.ru")
                .birthday(LocalDate.of(1985, 10, 17))
                .build();
        userController.create(user4);

        userController.addFriend(user.getId(), user2.getId());
        userController.addFriend(user4.getId(), user2.getId());
        userController.addFriend(user4.getId(), user3.getId());
        System.out.println(userController.getCommonFriends(user.getId(), user4.getId()));
        userController.removeFromFriends(user4.getId(), user2.getId());
        System.out.println(userController.getCommonFriends(user.getId(), user4.getId()));
    }

    @Test
    public void deleteFriend() {

        User user = User.builder()
                .name("Александр")
                .login("alex86")
                .email("al86mail@mail.ru")
                .birthday(LocalDate.of(1986, 10, 3))
                .build();
        userController.create(user);


        User user2 = User.builder()
                .name("Иван")
                .login("iva87")
                .email("iv87mail@mail.ru")
                .birthday(LocalDate.of(1987, 11, 9))
                .build();
        userController.create(user2);


        User user3 = User.builder()
                .name("Кирилл")
                .login("kir85")
                .email("kir85mail@mail.ru")
                .birthday(LocalDate.of(1985, 12, 7))
                .build();
        userController.create(user3);

        User user4 = User.builder()
                .name("Андрей")
                .login("an85")
                .email("andy85mail@mail.ru")
                .birthday(LocalDate.of(1985, 10, 17))
                .build();
        userController.create(user4);

        userController.addFriend(user.getId(), user2.getId());
        userController.addFriend(user4.getId(), user2.getId());
        userController.addFriend(user4.getId(), user3.getId());
        System.out.println(userController.getCommonFriends(user.getId(), user4.getId()));
        userController.deleteUser(user2.getId());
        System.out.println(userController.getCommonFriends(user.getId(), user4.getId()));
    }
}