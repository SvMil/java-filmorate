package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.sql.SQLException;
import java.util.HashMap;


@Slf4j
@Component("userDbStorage")
@Primary
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User create(User user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String querySql = "INSERT INTO \"USER\" (EMAIL, LOGIN, BIRTHDAY, NAME) VALUES (?, ?, ?, ?)";

        if (user.getName() == null || user.getName().isBlank() || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps =
                            connection.prepareStatement(querySql, new String[]{"user_id"});
                    ps.setString(1, user.getEmail());
                    ps.setString(2, user.getLogin());
                    ps.setDate(3, Date.valueOf(user.getBirthday()));
                    ps.setString(4, user.getName());
                    return ps;
                },
                keyHolder);
        user.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());

        return getUserById(user.getId());
    }

    @Override
    public User update(User user) {
        String querySql = "UPDATE \"USER\" SET EMAIL = ?, LOGIN = ?, BIRTHDAY = ?, NAME = ? WHERE USER_ID = ?";

        if (user.getName() == null || user.getName().isBlank() || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        jdbcTemplate.update(querySql, user.getEmail(), user.getLogin(), user.getBirthday(), user.getName(),
                user.getId());
        return getUserById(user.getId());
    }

    @Override
    public void deleteUser(long userId) {
        User user = getUserById(userId);
        String querySql = "DELETE FROM \"USER\" WHERE USER_ID = ?;";
        jdbcTemplate.update(querySql, userId);
    }

    @Override
    public User getUserById(long id) {
        String querySql = "SELECT * FROM \"USER\" WHERE USER_ID = ?";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(querySql, id);
        if (userRows.next()) {
            User user = User.builder()
                    .email(userRows.getString("EMAIL"))
                    .login(userRows.getString("LOGIN"))
                    .name(userRows.getString("NAME"))
                    .id(userRows.getLong("USER_ID"))
                    .birthday((Objects.requireNonNull(userRows.getDate("BIRTHDAY"))).toLocalDate())
                    .build();
            log.info("Пользователь с id {} найден", id);
            return user;
        }
        log.warn("Пользователь с id {} не найден", id);
        throw new NotFoundException("Пользователь не найден");
    }

    @Override
    public Map<Long, User> getUsers() {
        Map<Long, User> users = new HashMap<>();
        String querySql = "SELECT * FROM \"USER\"";
        List<User> usersFromDb = jdbcTemplate.query(querySql, this::rowToUser);
        for (User user : usersFromDb) {
            users.put(user.getId(), user);
        }
        return users;
    }

    @Override
    public void addFriend(long userId, long friendId) {
        User user = getUserById(userId);
        User friend = getUserById(friendId);
        String querySql = "INSERT INTO FRIENDS (FIRST_USER_ID, SECOND_USER_ID) VALUES (?, ?);";
        jdbcTemplate.update(querySql, userId, friendId);
    }

    public void addFriendWithApprove(long userId, long friendId) {
        User user = getUserById(userId);
        User friend = getUserById(friendId);
        String querySql = "INSERT INTO FRIENDS (FIRST_USER_ID, SECOND_USER_ID) VALUES (?, ?);";
        jdbcTemplate.update(querySql, userId, friendId);
    }

    @Override
    public List<User> getAllFriends(long userId) {
        User user = getUserById(userId);
        String querySql = "SELECT * FROM \"USER\" AS U WHERE U.USER_ID IN " +
                "(SELECT F.SECOND_USER_ID FROM FRIENDS AS F WHERE F.FIRST_USER_ID = ?);";
        return jdbcTemplate.query(querySql, this::rowToUser, userId);
    }

    @Override
    public void removeFromFriends(long userId, long friendId) {
        String querySql = "DELETE FROM FRIENDS WHERE FIRST_USER_ID = ? AND SECOND_USER_ID = ?;";
        jdbcTemplate.update(querySql, userId, friendId);
    }

    @Override
    public List<User> getCommonFriends(long userId, long otherUserId) {
        String querySql = "SELECT * FROM \"USER\" AS U WHERE U.USER_ID IN (SELECT F.SECOND_USER_ID " +
                "FROM FRIENDS AS F WHERE F.FIRST_USER_ID = ? " +
                "INTERSECT SELECT F.SECOND_USER_ID FROM FRIENDS AS F WHERE F.FIRST_USER_ID = ?);";
        return jdbcTemplate.query(querySql, this::rowToUser, userId, otherUserId);
    }

    private User rowToUser(ResultSet rs, int rowNum) throws SQLException {
        return User.builder()
                .email(rs.getString("EMAIL"))
                .login(rs.getString("LOGIN"))
                .name(rs.getString("NAME"))
                .id(rs.getLong("USER_ID"))
                .birthday((rs.getDate("BIRTHDAY")).toLocalDate())
                .build();
    }
}