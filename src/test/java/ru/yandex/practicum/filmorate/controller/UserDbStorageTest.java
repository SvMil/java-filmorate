package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.dao.FilmDbStorage;
import ru.yandex.practicum.filmorate.dao.UserDbStorage;

import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureTestDatabase
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserDbStorageTest {

    private final UserDbStorage userDbStorage;
    private final FilmDbStorage filmDbStorage;

    @Test
    public void testFindUserById() {
        userDbStorage.create(User.builder()
                .id(1L)
                .name("Андрей")
                .login("an85")
                .email("andy85mail@mail.ru")
                .birthday(LocalDate.of(1985, 10, 17))
                .build());

        Optional<User> userOptional = Optional.ofNullable(userDbStorage.getUserById(1));

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1L)
                );
    }


    @Test
    void getRecommendations() {
        userDbStorage.create(User.builder()
                .id(1L)
                .name("Андрей")
                .login("an85")
                .email("andy85mail@mail.ru")
                .birthday(LocalDate.of(1985, 10, 17))
                .build());

        userDbStorage.create(User.builder()
                .id(2L)
                .name("Кирилл")
                .login("kir85")
                .email("kir85mail@mail.ru")
                .birthday(LocalDate.of(1985, 12, 7))
                .build());

        userDbStorage.create(User.builder()
                .id(3L)
                .name("Александр")
                .login("alex86")
                .email("al86mail@mail.ru")
                .birthday(LocalDate.of(1986, 10, 3))
                .build());



        userDbStorage.create(User.builder()
                .id(4L)
                .name("Иван")
                .login("iva87")
                .email("iv87mail@mail.ru")
                .birthday(LocalDate.of(1987, 11, 9))
                .build());

        userDbStorage.addFriend(1L, 2L);
        userDbStorage.addFriend(4L, 2L);
        userDbStorage.addFriend(4L, 3L);
        System.out.println(userDbStorage.getCommonFriends(1L, 4L));
        userDbStorage.deleteUser(2L);
        System.out.println(userDbStorage.getCommonFriends(1L, 4L));

    }
}
