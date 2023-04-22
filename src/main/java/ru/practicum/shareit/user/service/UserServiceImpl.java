package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserJpaRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserJpaRepository userJpaRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public User create(UserDto userDto) {
        User user = userMapper.toEntity(userDto);
        return userJpaRepository.save(user);
    }

    @Override
    @Transactional
    public User update(Long userId, UserDto userDto) {
        User user = userJpaRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("user not found id=" + userId));
        if (userDto.getEmail() != null && !userDto.getEmail().isEmpty() && !userDto.getEmail().equals(user.getEmail())) {
            user.setEmail(userDto.getEmail());
        }
        if (userDto.getName() != null && !userDto.getName().isEmpty() && !userDto.getName().equals(user.getName())) {
            user.setName(userDto.getName());
        }
        return userJpaRepository.save(user);
    }

    @Override
    public List<User> getUsers() {
        return userJpaRepository.findAll();
    }

    @Override
    public User getUser(Long id) {
        return userJpaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("user not found id=" + id));
    }

    @Override
    @Transactional
    public Boolean remove(Long id) {
        userJpaRepository.deleteById(id);
        return true;
    }
}
