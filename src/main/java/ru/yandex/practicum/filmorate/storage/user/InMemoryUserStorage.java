package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;


import java.time.LocalDate;
import java.util.*;

@Component("inMemoryUserStorage")
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();

    @Override
    public User create(User user) {
        validateUser(user);
        user.setId(getNextId());
        users.put(user.getId(), user);
        return user;
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

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    @Override
    public User update(User newUser) {
        User oldUser = users.get(newUser.getId());
        validateUser(newUser);
        oldUser.setName(newUser.getName());
        oldUser.setEmail(newUser.getEmail());
        oldUser.setBirthday(newUser.getBirthday());
        oldUser.setLogin(newUser.getLogin());
        return oldUser;
    }

    @Override
    public void deleteUser(long userId) {
        users.remove(userId);
    }

    @Override
    public Map<Long, User> getUsers() {
        return users;
    }

    @Override
    public User getUserById(long id) {
        if (users.containsKey(id)) {
            return users.get(id);
        }
        return null;
    }

    @Override
    public void addFriend(long userId, long friendId) {
        User user = getUserById(userId);
        User friend = getUserById(friendId);
        if (user != null && friend != null) {
            user.getFriends().add(friendId);
            friend.getFriends().add(userId);
        }
    }

    @Override
    public void removeFromFriends(long userId, long friendId) {
        User user =getUserById(userId);
        User friend = getUserById(friendId);
        if (user != null && friend != null) {
            user.getFriends().remove(friendId);
            friend.getFriends().remove(userId);
        }
    }

    @Override
    public List<User> getCommonFriends(long userId, long otherUserId) {
        List<User> commonFriends = new ArrayList<>();
        User user = getUserById(userId);
        User otherUser = getUserById(otherUserId);
        if (user != null && otherUser != null) {
            Set<Long> commonFriendsIds = new HashSet<>(user.getFriends());
            commonFriendsIds.retainAll(otherUser.getFriends());
            for (Long id : commonFriendsIds) {
                commonFriends.add(getUserById(id));
            }
        }
        return commonFriends;
    }

    @Override
    public List<User> getAllFriends(long userId) {
        List<User> friends = new ArrayList<>();
        User user = getUserById(userId);
        if (user != null) {
            for (Long id : user.getFriends()) {
                friends.add(getUserById(id));
            }
        }
        return friends;
    }
}