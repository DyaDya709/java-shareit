package ru.practicum.shareit.user.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
class UserDtoTest {
    @Autowired
    private JacksonTester<UserDto> json;

    @Test
    @SneakyThrows
    public void userDtoTest() {
        UserDto user = new UserDto(1L, "user@user.ru", "Alex");
        String testData = String.format("{\"id\":1,\"email\":\"user@user.ru\",\"name\":\"Alex\"}");
        UserDto userDto = json.parseObject(testData);
        assertThat(userDto.equals(user)).isTrue();
    }
}