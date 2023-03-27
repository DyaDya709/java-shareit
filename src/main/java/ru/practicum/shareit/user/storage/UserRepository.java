package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserRepository {
    User create(User user);

    User update(Long userId, User user);

    User get(Long userId);

    List<User> getAll();

    Boolean remove(Long userId);

    Boolean isUniqEmail(String email);

    Boolean isUserPresent(Long userId);
}
