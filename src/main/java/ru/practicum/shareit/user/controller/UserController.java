package ru.practicum.shareit.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
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
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

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
    public User create(@Valid @RequestBody UserDto itemDto) {
        return userService.create(itemDto);
    }

    @PatchMapping("/{userId}")
    public User update(@PathVariable("userId") Optional<Long> id, @Valid @RequestBody UserDto itemDto) {
        if (!id.isPresent()) {
            throw new BadRequestException("id missing");
        }
        return userService.update(id.get(), itemDto);
    }

    @DeleteMapping("/{userId}")
    public Boolean remove(@PathVariable("userId") Optional<Long> id) {
        if (!id.isPresent()) {
            throw new BadRequestException("id missing");
        }
        return userService.remove(id.get());
    }
}
