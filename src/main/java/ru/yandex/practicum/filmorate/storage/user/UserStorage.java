package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Map;

public interface UserStorage {

    User create(User user);

    User update(User user);

    void deleteUser(long userId);

    User getUserById(long id);

    Map<Long, User> getUsers();

    void addFriend(long userId, long friendId);

    void removeFromFriends(long userId, long friendId);

    List<User> getCommonFriends(long userId, long otherUserId);

    List<User> getAllFriends(long userId);


}