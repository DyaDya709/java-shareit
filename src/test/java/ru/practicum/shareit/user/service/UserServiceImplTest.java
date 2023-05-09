package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
//@TestPropertySource(locations = "classpath:application.properties")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
class UserServiceImplTest {
    private final UserService userService;

    @Test
    @Order(1)
    void create() {
        UserDto userDto = UserDto.builder()
                .name("John")
                .email("john@gmail.com")
                .build();
        User user = userService.create(userDto);
        assertThat(user, notNullValue());
    }

    @Test
    @Order(2)
    void update() {
        UserDto userDto = UserDto.builder()
                .name("John")
                .email("updated@gmail.com")
                .build();
        User user = userService.update(1L, userDto);
        assertThat(user, notNullValue());
        assertThat(user.getId(), equalTo(1L));
    }

    @Test
    @Order(2)
    void updateWithInvalidUserId() {
        UserDto userDto = UserDto.builder()
                .name("John")
                .email("updated@gmail.com")
                .build();
        assertThrows(NotFoundException.class, () -> userService.update(99L, userDto));
    }


    @Test
    @Order(3)
    void getUsers() {
        List<User> users = userService.getUsers();
        assertThat(users, hasSize(1));
    }

    @Test
    @Order(4)
    void getUser() {
        User user = userService.getUser(1L);
        assertThat(user, notNullValue());
    }

    @Test
    @Order(4)
    void getUserByInvalidId() {
        assertThrows(NotFoundException.class, () -> userService.getUser(99L));
    }

    @Test
    @Order(5)
    void remove() {
        Boolean result = userService.remove(1L);
        assertTrue(result);
    }
}