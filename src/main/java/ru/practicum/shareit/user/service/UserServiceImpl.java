package ru.practicum.shareit.user.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.List;

@Service
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User create(UserDto userDto) {
        if (userDto.getEmail() == null || userDto.getEmail().isEmpty()) {
            throw new BadRequestException("empty email");
        }
        if (userRepository.isUniqEmail(userDto.getEmail())) {
            return userRepository.create(UserMapper.makeUser(userDto));
        }
        throw new ConflictException(String.format("mail %s is not unique",userDto.getEmail()));
    }

    @Override
    public User update(Long userId, UserDto userDto) {
        return userRepository.update(userId, UserMapper.makeUser(userDto));
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
