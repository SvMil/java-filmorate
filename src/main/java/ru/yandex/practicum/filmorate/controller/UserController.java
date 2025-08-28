package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Long, User> users = new HashMap<>();
    @GetMapping
    public Collection<User> findAll(){
        return users.values();
    }

    @PostMapping
    public User  create(@RequestBody User user) {
        // проверяем выполнение необходимых условий
        if (user.getLogin() == null || user.getLogin().isBlank()) {
            throw new ConditionsNotMetException("Логин не может быть пустым");
        }
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new ConditionsNotMetException("Почта не может быть пустой");
        }
        if (!user.getEmail().contains("@")) {
            throw new ConditionsNotMetException("Почта должна содержать символ @");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
          if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ConditionsNotMetException("Дата рождения не может быть в будущем");
        }
        user.setId(getNextId());
        users.put(user.getId(), user);
        return user;
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    @PutMapping
    public User update(@RequestBody User newUser) {
        // проверяем необходимые условия
        if (newUser.getId() == null) {
            throw new ConditionsNotMetException("Id должен быть указан");
        }
        if (users.containsKey(newUser.getId())) {
            User oldUser = users.get(newUser.getId());
            if (newUser.getLogin() == null || newUser.getLogin().isBlank()) {
                throw new ConditionsNotMetException("Логин не может быть пустым");
            }
            if (newUser.getEmail() == null || newUser.getEmail().isBlank()) {
                throw new ConditionsNotMetException("Почта не может быть пустой");
            }
            if (!newUser.getEmail().contains("@")) {
                throw new ConditionsNotMetException("Почта должна содержать символ @");
            }
            if (newUser.getName() == null || newUser.getName().isBlank()) {
                newUser.setName(newUser.getLogin());
            }
            if (newUser.getBirthday().isAfter(LocalDate.now())) {
                throw new ConditionsNotMetException("Дата рождения не может быть в будущем");
            }
            oldUser.setName(newUser.getName());
            oldUser.setEmail(newUser.getEmail());
            oldUser.setBirthday(newUser.getBirthday());
            oldUser.setLogin(newUser.getLogin());
            return oldUser;
        }
        throw new NotFoundException("Пользователь с id = " + newUser.getId() + " не найден");
    }
}
