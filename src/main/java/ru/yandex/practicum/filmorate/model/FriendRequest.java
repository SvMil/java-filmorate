package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FriendRequest {
    private Long idFriend1;
    private Long idFriend2;
    private boolean isApproved;
}
