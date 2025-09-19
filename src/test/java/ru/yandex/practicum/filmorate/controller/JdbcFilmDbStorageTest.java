package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.dao.FilmDbStorage;
import ru.yandex.practicum.filmorate.dao.UserDbStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({FilmDbStorage.class, UserDbStorage.class})
class JdbcFilmDbStorageTest {

    private final UserDbStorage userDbStorage;
    private final FilmDbStorage filmDbStorage;

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

        filmDbStorage.create(Film.builder().id(1L).name("Анора").description("Американская драма").duration(139)
                .releaseDate(LocalDate.now()).mpa(new Mpa(1, "Драма")).build());
        filmDbStorage.create(Film.builder().id(2L).name("Области тьмы").description("Триллер").duration(105)
                .releaseDate(LocalDate.now()).mpa(new Mpa(1, "Триллер")).build());
        filmDbStorage.create(Film.builder().id(3L).name("Бегущий по лезвию").description("Драма США").duration(164)
                .releaseDate(LocalDate.now()).mpa(new Mpa(2, "Драма")).build());
        filmDbStorage.addLike(1, 1);
        filmDbStorage.addLike(2, 1);
        filmDbStorage.addLike(2, 2);
        filmDbStorage.addLike(3, 1);
        filmDbStorage.addLike(3, 2);
        filmDbStorage.addLike(3, 3);

        List<Integer> likes = filmDbStorage.getLikesOfFilm(2L);
        assertEquals(2, likes.size());

        System.out.println(filmDbStorage.getFilmsSortedByLikes());
        List<Film> films = filmDbStorage.getFilmsSortedByLikes();
        assertEquals(3, films.get(0).getId());

        System.out.println(filmDbStorage.getFilms());
        Map<Long, Film> getFilms = filmDbStorage.getFilms();
        assertEquals(2, getFilms.get(2L).getLikes().size());
    }
}
