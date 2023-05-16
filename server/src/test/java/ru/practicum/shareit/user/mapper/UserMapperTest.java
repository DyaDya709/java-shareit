package ru.practicum.shareit.user.mapper;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

class UserMapperTest {

    @Test
    void toDto() {
        User user = new User();
        user.setId(1L);
        user.setName("John");
        user.setEmail("john@gmail.com");
        UserDto userDto = UserMapper.toDto(user);
        assertThat(userDto, notNullValue());
    }

    @Test
    void toEntity() {
        UserDto userDto = UserDto.builder()
                .name("John")
                .email("john@gmail.com")
                .build();
        User user = UserMapper.toEntity(userDto);
        assertThat(user, notNullValue());
    }
}