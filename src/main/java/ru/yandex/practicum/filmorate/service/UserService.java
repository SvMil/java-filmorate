package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.exception.*;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User create(User user) {
        for (User registeredUser : userStorage.getUsers().values()) {
            if (registeredUser.getEmail().equals(user.getEmail())) {
                log.warn("Пользователь с электронной почтой " + user.getEmail()
                        + " уже зарегистрирован");
                throw new ValidationException();
            }
        }
        validateUser(user);
        log.info("Добавлен новый пользователь c {} login {} ", user.getName(), user.getLogin());
        return userStorage.create(user);
    }

    public User update(User user) {
        if (userStorage.getUserById(user.getId()) == null) {
            log.warn("Невозможно обновить пользователя");
            throw new NotFoundException("Пользователь не найден");
        }
        validateUser(user);
        log.info("Пользователь {} с id {} обновлён", user.getName(), user.getId());
        return userStorage.update(user);
    }

    public void deleteUser(long userId) {
        userStorage.deleteUser(userId);
        log.info("Пользователь с id {} , был удалён", userId);
    }

    public Collection<User> getUsers() {
        log.info("Получение списка пользователей");
        return Collections.unmodifiableCollection(userStorage.getUsers().values());
    }

    public User getUserById(long id) {
        User user = userStorage.getUserById(id);
        if (user == null) {
            log.warn("Пользователя с id {} не найдено", id);
            throw new NotFoundException("Пользователь не найден");
        }
        log.info("Получение информации о пользователе с id {} ", user.getId());
        return user;
    }

    public void addFriend(long userId, long friendId) {
        if ((userStorage.getUserById(userId) == null) || (userStorage.getUserById(friendId) == null)) {
            throw new NotFoundException("Пользователь не найден");
        }
        userStorage.addFriend(userId, friendId);
        log.info("Пользователи с id {} и {} теперь друзья", userId, friendId);
    }

    public void removeFromFriends(long userId, long friendId) {
        if ((userStorage.getUserById(userId) == null) || (userStorage.getUserById(friendId) == null)) {
            throw new NotFoundException("Пользователь не найден");
        }
        userStorage.removeFromFriends(userId, friendId);
        log.info("Пользователи с id {} и {} теперь не являются друзьями", userId, friendId);
    }

    public List<User> getCommonFriends(long userId, long otherUserId) {
        if ((userStorage.getUserById(userId) == null) || (userStorage.getUserById(otherUserId) == null)) {
            throw new NotFoundException("Пользователь не найден");
        }
        log.info("Получение списка общих друзей пользователей с id {} и с id {} ", userId, otherUserId);
        return userStorage.getCommonFriends(userId, otherUserId);
    }

    public List<User> getAllFriends(long userId) {
        if (userStorage.getUserById(userId) == null) {
            throw new NotFoundException("Пользователь не найден");
        }
        return userStorage.getAllFriends(userId);
    }

    private void validateUser(User user) {
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
    }

}