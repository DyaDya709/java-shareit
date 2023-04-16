package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    private final UserMapper userMapper;

    @Override
    public User create(UserDto userDto) {
        if (userRepository.isUniqEmail(userDto.getEmail())) {
            return userRepository.create(userMapper.toEntity(userDto));
        }
        throw new ConflictException(String.format("mail %s is not unique", userDto.getEmail()));
    }

    @Override
    public User update(Long userId, UserDto userDto) {
        return userRepository.update(userId, userMapper.toEntity(userDto));
    }

    @Override
    public List<User> getUsers() {
        return userRepository.getAll();
    }

    @Override
    public User getUser(Long id) {
        return userRepository.get(id);
    }

    @Override
    public Boolean remove(Long id) {
        return userRepository.remove(id);
    }
}
