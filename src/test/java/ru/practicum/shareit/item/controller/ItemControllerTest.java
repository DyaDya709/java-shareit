package ru.practicum.shareit.item.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.mapper.CommentMapper;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.comment.service.CommentService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
@AutoConfigureMockMvc
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private final ItemService itemService;

    @MockBean
    private final CommentService commentService;

    @MockBean
    private final UserService userService;

    private static final String userIdRequestHeader = "X-Sharer-User-Id";

    ItemDto itemDto;
    private Item item;
    private Item itemUpdate;
    private User owner;
    private User user;
    private CommentDto commentDto;
    private Comment comment;
    final Long userId = 1L;
    final Long itemId = 1L;
    final Pageable pageable = PageRequest.of(0, 2);


    @BeforeEach
    private void init() {
        owner = new User();
        owner.setId(1L);
        owner.setName("user1");
        owner.setEmail("user1@user.ru");

        user = new User();
        user.setId(1L);
        user.setName("user2");
        user.setEmail("user2@user.ru");

        item = new Item();
        item.setName("item1");
        item.setDescription("item1Description");
        item.setAvailable(true);

        itemDto = ItemDto.builder()
                .name("item1")
                .description("item1Description")
                .available(true)
                .build();

        itemUpdate = new Item();
        itemUpdate.setName("itemNew");
        itemUpdate.setDescription("item1DescNew");
        itemUpdate.setAvailable(false);

        commentDto = new CommentDto();
        commentDto.setId(1L);
        commentDto.setAuthor("I am");
        commentDto.setText("Comment");
        commentDto.setCreated(LocalDateTime.parse("2024-10-23T17:19:33"));

        comment = CommentMapper.toEntity(commentDto, user, item);
    }


    @Test
    @SneakyThrows
    void create() {
        when(userService.getUser(userId)).thenReturn(owner);
        when(itemService.create(user.getId(), itemDto)).thenReturn(item);
        String result = mockMvc.perform(post("/items")
                        .content(objectMapper.writeValueAsString(itemDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(userIdRequestHeader, userId))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertEquals(objectMapper.writeValueAsString(item), result);
    }

    @Test
    @SneakyThrows
    void createWithInvalidItem() {
        ItemDto itemDto2 = ItemDto.builder()
                .build();
        mockMvc.perform(post("/items")
                        .content(objectMapper.writeValueAsString(itemDto2))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(userIdRequestHeader, userId))
                .andExpect(status().is4xxClientError());
        verify(itemService, never()).create(any(), any());
    }

    @Test
    @SneakyThrows
    void createWithInvalidUserId() {
        mockMvc.perform(post("/items")
                        .content(objectMapper.writeValueAsString(itemDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
        verify(itemService, never()).create(any(), any());
    }

    @Test
    @SneakyThrows
    void createWithInvalidItemAndUser() {
        mockMvc.perform(post("/items")
                        .content(objectMapper.writeValueAsString(null))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
        verify(itemService, never()).create(any(), any());
    }

    @Test
    @SneakyThrows
    void createWithInvalidItemName() {
        itemDto = ItemDto.builder()
                .name(null)
                .description("item1Desc")
                .available(true)
                .build();
        mockMvc.perform(post("/items")
                        .content(objectMapper.writeValueAsString(itemDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
        verify(itemService, never()).create(any(), any());
    }

    @Test
    @SneakyThrows
    void createWithInvalidItemDesc() {
        itemDto = ItemDto.builder()
                .name("item1")
                .description(null)
                .available(true)
                .build();
        mockMvc.perform(post("/items")
                        .content(objectMapper.writeValueAsString(itemDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
        verify(itemService, never()).create(any(), any());
    }

    @Test
    @SneakyThrows
    void createWithInvalidItemAvailable() {
        itemDto = ItemDto.builder()
                .name("item1")
                .description("description")
                .available(null)
                .build();
        mockMvc.perform(post("/items")
                        .content(objectMapper.writeValueAsString(itemDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
        verify(itemService, never()).create(any(), any());
    }

    @Test
    @SneakyThrows
    void update() {
        when(itemService.update(userId, userId, itemDto)).thenReturn(item);
        String result = mockMvc.perform(patch("/items/{itemId}", itemId)
                        .content(objectMapper.writeValueAsString(itemDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(userIdRequestHeader, userId))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertEquals(objectMapper.writeValueAsString(item), result);
    }

    @Test
    @SneakyThrows
    void updateWithInvalidItem() {
        mockMvc.perform(patch("/items/{itemId}", itemId)
                        .content(objectMapper.writeValueAsString(null))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(userIdRequestHeader, userId))
                .andExpect(status().is4xxClientError());
        verify(itemService, never()).update(any(), any(), any());
    }

    @Test
    @SneakyThrows
    void updateWithInvalidUserId() {
        mockMvc.perform(patch("/items/{itemId}", itemId)
                        .content(objectMapper.writeValueAsString(itemDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
        verify(itemService, never()).update(any(), any(), any(ItemDto.class));
    }

    @Test
    @SneakyThrows
    void updateWithInvalidItemId() {
        mockMvc.perform(patch("/items/{itemId}", "")
                        .content(objectMapper.writeValueAsString(itemDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(userIdRequestHeader, userId))
                .andExpect(status().is4xxClientError());
        verify(itemService, never()).update(any(), any(), any(ItemDto.class));
    }

    @Test
    @SneakyThrows
    void getItemById() {
        when(itemService.get(itemId, userId)).thenReturn(item);
        String result = mockMvc.perform(get("/items/{itemId}", itemId)
                        .header(userIdRequestHeader, userId))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(itemService, times(1)).get(any(), any());
        assertEquals(objectMapper.writeValueAsString(item), result);
    }

    @Test
    @SneakyThrows
    void getItemByIdWithInvalidItemId() {
        mockMvc.perform(get("/items/{itemId}", "o")
                        .header(userIdRequestHeader, userId))
                .andExpect(status().is4xxClientError());
        verify(itemService, never()).get(any(), any());
    }

    @Test
    @SneakyThrows
    void getItemByIdWithInvalidUserId() {
        mockMvc.perform(get("/items/{itemId}", itemId))
                .andExpect(status().is4xxClientError());
        verify(itemService, never()).get(any(), any());
    }

    @Test
    @SneakyThrows
    void getItemByIdWithInvalidUserIdAndInvalidItemId() {
        mockMvc.perform(get("/items/{itemId}", ""))
                .andExpect(status().is4xxClientError());
        verify(itemService, never()).get(any(), any());
    }

    @Test
    @SneakyThrows
    void getAllItem() {
        List<Item> itemList = Collections.singletonList(item);
        when(itemService.getAll(userId, pageable)).thenReturn(itemList);
        String result = mockMvc.perform(get("/items")
                        .param("from", "0")
                        .param("size", "2")
                        .header(userIdRequestHeader, userId))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(itemService, times(1)).getAll(any(), any());
        assertEquals(objectMapper.writeValueAsString(itemList), result);
    }

    @Test
    @SneakyThrows
    void getAllItemWithInvalidUserId() {
        mockMvc.perform(get("/items")
                        .param("from", "0")
                        .param("size", "2"))
                .andExpect(status().is4xxClientError());
        verify(itemService, never()).getAll(any(), any());
    }

    @Test
    @SneakyThrows
    void findAvailable() {
        List<Item> itemList = Collections.singletonList(item);
        when(itemService.findAvailable("text", pageable)).thenReturn(itemList);
        String result = mockMvc.perform(get("/items/search")
                        .param("from", "0")
                        .param("size", "2")
                        .param("text", "text")
                        .header(userIdRequestHeader, userId))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(itemService, times(1)).findAvailable(any(), any());
        assertEquals(objectMapper.writeValueAsString(itemList), result);
    }

    @Test
    @SneakyThrows
    void findAvailableWithEmptyText() {
        List<Item> itemList = Collections.emptyList();
        when(itemService.findAvailable("", pageable)).thenReturn(itemList);
        String result = mockMvc.perform(get("/items/search")
                        .param("from", "0")
                        .param("size", "2")
                        .param("text", "")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(itemService, times(1)).findAvailable("", pageable);
        assertEquals(objectMapper.writeValueAsString(itemList), result);
    }

    @Test
    @SneakyThrows
    void createComment() {
        when(commentService.createComment(itemId, userId, commentDto)).thenReturn(comment);
        String result = mockMvc.perform(post("/items/{itemId}/comment", itemId)
                        .content(objectMapper.writeValueAsString(commentDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(userIdRequestHeader, userId))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(commentService, times(1)).createComment(itemId, userId, commentDto);
        assertEquals(objectMapper.writeValueAsString(comment), result);
    }

    @Test
    @SneakyThrows
    void createCommentWithInvalidId() {
        mockMvc.perform(post("/items/{itemId}/comment", itemId)
                        .content(objectMapper.writeValueAsString(commentDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(userIdRequestHeader, ""))
                .andExpect(status().is4xxClientError());
        verify(commentService, never()).createComment(any(), any(), any());
    }
}