package ru.practicum.shareit.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private UserService userService;
    private final Long userId = 1L;
    private User user;
    private UserDto userDto;
    private UserDto userDtoInvalidEmail;
    private UserDto userDtoNullEmail;
    private UserDto userDtoEmptyName;

    @BeforeEach
    private void init() {
        user = new User();
        user.setId(1L);
        user.setName("user1");
        user.setEmail("user@user.ru");

        userDto = UserDto.builder()
                .id(1L)
                .name("user1")
                .email("user@user.ru")
                .build();
        userDtoInvalidEmail = UserDto.builder()
                .id(1L)
                .name("Jonn")
                .email("useruser.ru")
                .build();
        userDtoNullEmail = UserDto.builder()
                .id(1L)
                .name("user1")
                .email(null)
                .build();
        userDtoEmptyName = UserDto.builder()
                .id(1L)
                .name(null)
                .email("user@user.ru")
                .build();
    }

    @Test
    void create() throws Exception {
        when(userService.create(userDto)).thenReturn(user);
        String result = mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(userDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(userService, times(1)).create(any());
        assertEquals(objectMapper.writeValueAsString(userDto), result);
    }

    @Test
    void createWithInvalidEmail() throws Exception {
        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(userDtoInvalidEmail))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
        verify(userService, never()).create(any());
    }

    @Test
    void createWithNullEmail() throws Exception {
        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(userDtoNullEmail))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
        verify(userService, never()).create(any());
    }


    @Test
    void createWithNullDto() throws Exception {
        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(null))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
        verify(userService, never()).create(any());
    }

    @Test
    void update() throws Exception {
        when(userService.update(userId, userDtoNullEmail)).thenReturn(user);
        String result = mockMvc.perform(patch("/users/{id}", userId)
                        .content(objectMapper.writeValueAsString(userDtoNullEmail))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(userService, times(1)).update(userId, userDtoNullEmail);
        assertEquals(objectMapper.writeValueAsString(user), result);
    }

    @Test
    void updateWithEmptyName() throws Exception {
        when(userService.update(userId, userDtoEmptyName)).thenReturn(user);
        String result = mockMvc.perform(patch("/users/{id}", userId)
                        .content(objectMapper.writeValueAsString(userDtoEmptyName))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(userService, times(1)).update(userId, userDtoEmptyName);
        assertEquals(objectMapper.writeValueAsString(user), result);
    }

    @Test
    void updateWithNullDto() throws Exception {
        mockMvc.perform(patch("/users/{id}", userId)
                        .content(objectMapper.writeValueAsString(null))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
        verify(userService, never()).update(any(), any());
    }

    @Test
    void updateWithInvalidUserID() throws Exception {
        mockMvc.perform(patch("/users/{id}", "")
                        .content(objectMapper.writeValueAsString(userDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
        verify(userService, never()).update(any(), any());
    }

    @Test
    void updateWithNotFoundException() throws Exception {
        when(userService.getUser(userId)).thenThrow(
                new NotFoundException("Пользователь с ID " + userId + " не найден"));
        mockMvc.perform(patch("/users/{id}", userId))
                .andExpect(status().is4xxClientError());
    }


    @Test
    void getUsers() throws Exception {
        List<User> users = Arrays.asList(user);
        when(userService.getUsers()).thenReturn(users);
        String result = mockMvc.perform(get("/users")
                        .content(objectMapper.writeValueAsString(users))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(userService, times(1)).getUsers();
        assertEquals(objectMapper.writeValueAsString(users), result);
    }

    @Test
    void getUser() throws Exception {
        when(userService.getUser(userId)).thenReturn(user);
        String result = mockMvc.perform(get("/users/{id}", userId)
                        .content(objectMapper.writeValueAsString(userDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(userService, times(1)).getUser(userId);
        assertEquals(objectMapper.writeValueAsString(userDto), result);
    }

    @Test
    void getUserWithNotFoundException() throws Exception {
        when(userService.getUser(userId)).thenThrow(
                new NotFoundException("Пользователь с ID " + userId + " не найден"));
        mockMvc.perform(get("/users/{id}", userId))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void getUserWithInvalidId() throws Exception {
        mockMvc.perform(get("/users/{id}", "q"))
                .andExpect(status().is4xxClientError());
        verify(userService, never()).getUser(userId);
    }

    @Test
    void remove() throws Exception {
        mockMvc.perform(delete("/users/{id}", userId))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void removeWithInvalidUserId() throws Exception {
        mockMvc.perform(delete("/users/{id}", "e"))
                .andExpect(status().is4xxClientError());
    }
}