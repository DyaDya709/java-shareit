package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {
    User create(UserDto itemDto);

    User update(Long userId, UserDto itemDto);

    List<User> getUsers();

    User getUser(Long id);

    Boolean remove(Long id);
}
