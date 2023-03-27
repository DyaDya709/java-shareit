package ru.practicum.shareit.user.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository USER_REPOSITORY;

    public UserServiceImpl(UserRepository USER_REPOSITORY) {
        this.USER_REPOSITORY = USER_REPOSITORY;
    }

    @Override
    public User create(UserDto userDto) {
        if (USER_REPOSITORY.isUniqEmail(userDto.getEmail())) {
            return USER_REPOSITORY.create(UserMapper.makeUser(userDto));
        }
        throw new ConflictException(String.format("mail %s is not unique", userDto.getEmail()));
    }

    @Override
    public User update(Long userId, UserDto userDto) {
        return USER_REPOSITORY.update(userId, UserMapper.makeUser(userDto));
    }

    @Override
    public List<User> getUsers() {
        return USER_REPOSITORY.getAll();
    }

    @Override
    public User getUser(Long id) {
        return USER_REPOSITORY.get(id);
    }

    @Override
    public Boolean remove(Long id) {
        return USER_REPOSITORY.remove(id);
    }
}
