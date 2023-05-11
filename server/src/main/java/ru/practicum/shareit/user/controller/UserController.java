package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<User> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/{userId}")
    public User getUser(@PathVariable("userId") Long userId) {
//        if (!id.isPresent()) {
//            throw new BadRequestException("id missing");
//        }
        return userService.getUser(userId);
    }

    @PostMapping()
    public User create(@RequestBody UserDto userDto) {
        return userService.create(userDto);
    }

    @PatchMapping("/{userId}")
    public User update(@PathVariable("userId") Long id, @RequestBody UserDto userDto) {
//        if (!id.isPresent()) {
//            throw new BadRequestException("id missing");
//        }
        return userService.update(id, userDto);
    }

    @DeleteMapping("/{userId}")
    public Boolean remove(@PathVariable("userId") Long id) {
//        if (!id.isPresent()) {
//            throw new BadRequestException("id missing");
//        }
        return userService.remove(id);
    }
}
