package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.dao.FilmDbStorage;
import ru.yandex.practicum.filmorate.dao.UserDbStorage;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureTestDatabase
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmDbStorageTest {

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

        filmDbStorage.create(Film.builder().id(1L).name("Анора").description("Американская драма").duration(139)
                .releaseDate(LocalDate.now()).mpa(new Mpa(1, "Драма")).build());
        filmDbStorage.create(Film.builder().id(2L).name("Области тьмы").description("Триллер").duration(105)
                .releaseDate(LocalDate.now()).mpa(new Mpa(1, "Триллер")).build());

        filmDbStorage.addLike(1, 1);
        filmDbStorage.addLike(2, 1);
        filmDbStorage.addLike(2, 2);

        System.out.println(filmDbStorage.getLikesOfFilm(2L));

        System.out.println(filmDbStorage.getFilms());

        System.out.println(userDbStorage.getUsers());
    }
}
