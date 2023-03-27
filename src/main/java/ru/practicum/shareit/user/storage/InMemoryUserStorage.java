package ru.practicum.shareit.user.storage;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class InMemoryUserStorage implements UserRepository {
    private final HashMap<String, User> userStorage = new HashMap<>();

    private Long newId = 0L;

    private long getNewId() {
        return ++newId;
    }

    @Override
    public User create(User user) {
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new BadRequestException("mail can't be empty");
        }
        if (!isUniqEmail(user.getEmail())) {
            throw new ConflictException(String.format("mail %s is not unique", user.getEmail()));
        }
        user.setId(getNewId());
        userStorage.put(user.getEmail(), user);
        return user;
    }

    @Override
    public User update(Long userId, User user) {
        User presentUser = userStorage.values().stream()
                .filter(u -> u.getId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(String.format("user with id %s not found", userId)));
        if (user.getEmail() != null && !user.getEmail().isEmpty() && !user.getEmail().equals(presentUser.getEmail())) {
            if (!isUniqEmail(user.getEmail())) {
                throw new ConflictException(String.format("mail %s is not unique", user.getEmail()));
            }
            String oldEmail = presentUser.getEmail();
            presentUser.setEmail(user.getEmail());
            userStorage.remove(oldEmail);
            userStorage.put(presentUser.getEmail(), presentUser);
        }
        if (user.getName() != null && !user.getName().isEmpty() && !user.getName().equals(presentUser.getName())) {
            presentUser.setName(user.getName());
        }
        return presentUser;
    }

    @Override
    public User get(Long userId) {
        return userStorage.values().stream()
                .filter(user -> user.getId().equals(userId))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<User> getAll() {
        return userStorage.values().stream().collect(Collectors.toList());
    }

    @Override
    public Boolean remove(Long userId) {
        userStorage.values().removeIf(user -> user.getId().equals(userId));
        return true;
    }

    @Override
    public Boolean isUniqEmail(String email) {
        return !userStorage.containsKey(email);
    }

    @Override
    public Boolean isUserPresent(Long userId) {
        return userStorage.values().stream()
                .filter(u -> u.getId().equals(userId))
                .findFirst()
                .isPresent();
    }
}
