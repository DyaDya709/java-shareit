package ru.practicum.shareit.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.advice.ApplicationExceptionsHandler;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collections;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    @Mock
    private UserService userService;
    @InjectMocks
    private UserController controller;

    private MockMvc mvc;
    private User user;
    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(new ApplicationExceptionsHandler())
                .build();
        user = new User();
        user.setId(1L);
        user.setName("user name");
        user.setEmail("user@user.ru");
    }

    @Test
    void getUsers() throws Exception {
        when(userService.getUsers()).thenReturn(Collections.singletonList(user));
        mvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(user.getId()), Long.class))
                .andExpect(jsonPath("$[0].name", is(user.getName())))
                .andExpect(jsonPath("$[0].email", is(user.getEmail())));
    }

    @Test
    void getUser() throws Exception {
        when(userService.getUser(anyLong())).thenReturn(user);
        mvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(user.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(user.getName())))
                .andExpect(jsonPath("$.email", is(user.getEmail())));
    }

    @Test
    void getUserWithThrow() throws Exception {
        doThrow(new NotFoundException("User not found")).when(userService).getUser(anyLong());
        mvc.perform(get("/users/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void create() throws Exception {
        UserDto userDto = UserDto.builder()
                .name("user name")
                .email("user@user.ru")
                .build();
        when(userService.create(any(UserDto.class))).thenReturn(user);
        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(user.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(user.getName())))
                .andExpect(jsonPath("$.email", is(user.getEmail())));
    }

    @Test
    void createWithInvalidEmail() throws Exception {
        UserDto userDto = UserDto.builder()
                .name("user name")
                .email("user.ru")
                .build();
        MvcResult result = mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andReturn();
        String responseContent = result.getResponse().getContentAsString();
        assertThat(responseContent).contains("invalid email address");
    }

    @Test
    void update() throws Exception {
        UserDto userDto = UserDto.builder()
                .name("user name updated")
                .build();
        when(userService.update(anyLong(), any(UserDto.class))).thenReturn(user);
        mvc.perform(patch("/users/1")
                        .content(mapper.writeValueAsString(userDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(user.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(user.getName())))
                .andExpect(jsonPath("$.email", is(user.getEmail())));
    }

    @Test
    void remove() throws Exception {
        when(userService.remove(anyLong())).thenReturn(true);
        mvc.perform(delete("/users/1"))
                .andExpect(status().isOk());
    }
}