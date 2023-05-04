package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<User> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/{userId}")
    public User getUser(@PathVariable("userId") Optional<Long> id) {
        if (!id.isPresent()) {
            throw new BadRequestException("id missing");
        }
        return userService.getUser(id.get());
    }

    @PostMapping()
    public User create(@Valid @RequestBody UserDto userDto) {
        return userService.create(userDto);
    }

    @PatchMapping("/{userId}")
    public User update(@PathVariable("userId") Optional<Long> id, @RequestBody UserDto userDto) {
        if (!id.isPresent()) {
            throw new BadRequestException("id missing");
        }
        return userService.update(id.get(), userDto);
    }

    @DeleteMapping("/{userId}")
    public Boolean remove(@PathVariable("userId") Optional<Long> id) {
        if (!id.isPresent()) {
            throw new BadRequestException("id missing");
        }
        return userService.remove(id.get());
    }
}
