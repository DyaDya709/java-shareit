package ru.practicum.shareit.request.controller;

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
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.response.model.ItemResponseDto;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemRequestController.class)
@AutoConfigureMockMvc
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemRequestControllerTest {
    private final String requestHeaderUserId = "X-Sharer-User-Id";
    @MockBean
    private final ItemRequestService itemRequestService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private ItemRequestDto itemRequestDto;
    private ItemResponseDto answerDto;
    private User user;
    final Long userId = 1L;
    final Long ownerId = 2L;
    final Long requestId = 2L;
    final Pageable pageable = PageRequest.of(0, 2);

    @BeforeEach
    private void init() {
        answerDto = new ItemResponseDto();
        answerDto.setId(1L);
        answerDto.setItemName("answer");
        answerDto.setDescription("answer text");
        answerDto.setAvailable(true);
        answerDto.setRequestId(ownerId);
        itemRequestDto = new ItemRequestDto();
        itemRequestDto.setId(1L);
        itemRequestDto.setDescription("req1");
        user = new User();
        user.setId(3L);
        user.setName("user");
        user.setEmail("user@user.ru");
    }

    @Test
    @SneakyThrows
    void create() {
        when(itemRequestService.create(any())).thenReturn(itemRequestDto);
        String result = mockMvc.perform(post("/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemRequestDto))
                        .header(requestHeaderUserId, 1))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(itemRequestService).create(any());
        assertEquals(objectMapper.writeValueAsString(itemRequestDto), result);
    }

    @Test
    @SneakyThrows
    void createWithNullRequest() {
        mockMvc.perform(post("/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(null))
                        .header(requestHeaderUserId, 1))
                .andExpect(status().is4xxClientError());
        verify(itemRequestService, never()).create(any());
    }

    @Test
    @SneakyThrows
    void createWithNullDescription() {
        ItemRequestDto request = new ItemRequestDto();
        request.setId(1L);
        request.setDescription(null);
        mockMvc.perform(post("/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header(requestHeaderUserId, 1))
                .andExpect(status().is4xxClientError());
        verify(itemRequestService, never()).create(any());
    }

    @Test
    @SneakyThrows
    void createWithInvalidUserId() {
        mockMvc.perform(post("/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemRequestDto)))
                .andExpect(status().is4xxClientError());
        verify(itemRequestService, never()).create(any());
    }

    @Test
    @SneakyThrows
    void getOwnRequests() {
        final List<ItemRequestDto> requestDtoList = Arrays.asList(itemRequestDto);
        when(itemRequestService.getOwnRequests(userId)).thenReturn(requestDtoList);
        String result = mockMvc.perform(get("/requests")
                        .header(requestHeaderUserId, 1))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(itemRequestService).getOwnRequests(any());
        assertEquals(objectMapper.writeValueAsString(requestDtoList), result);
    }

    @Test
    @SneakyThrows
    void getOwnRequestsWithInvalidUserId() {
        final List<ItemRequestDto> requestDtoList = Arrays.asList(itemRequestDto);
        when(itemRequestService.getOwnRequests(userId)).thenReturn(requestDtoList);
        mockMvc.perform(get("/requests")
                        .header(requestHeaderUserId, "o"))
                .andExpect(status().is4xxClientError());
        verify(itemRequestService, never()).getOwnRequests(any());
    }

    @Test
    @SneakyThrows
    void getOwnRequestsWhenEmptyListRequestDto() {
        final List<ItemRequestDto> requestDtoList = new ArrayList<>();
        when(itemRequestService.getOwnRequests(userId)).thenReturn(requestDtoList);
        String result = mockMvc.perform(get("/requests")
                        .header(requestHeaderUserId, 1))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(itemRequestService).getOwnRequests(any());
        assertEquals(objectMapper.writeValueAsString(requestDtoList), result);
    }

    @Test
    @SneakyThrows
    void getOtherRequests() {
        final List<ItemRequestDto> requestDtoList = Arrays.asList(itemRequestDto);
        when(itemRequestService.getOtherRequests(userId, pageable)).thenReturn(requestDtoList);
        String result = mockMvc.perform(get("/requests/all")
                        .param("from", "0")
                        .param("size", "2")
                        .header(requestHeaderUserId, userId))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(itemRequestService, times(1)).getOtherRequests(userId, pageable);
        assertEquals(objectMapper.writeValueAsString(requestDtoList), result);
    }

    @Test
    @SneakyThrows
    void getOtherRequestsWithNullFrom() {
        final List<ItemRequestDto> requestDtoList = Collections.emptyList();
        when(itemRequestService.getOtherRequests(userId, pageable)).thenReturn(requestDtoList);
        String result = mockMvc.perform(get("/requests/all")
                        .param("from", "0")
                        .param("size", "2")
                        .header(requestHeaderUserId, userId))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(itemRequestService, times(1)).getOtherRequests(userId, pageable);
        assertEquals(objectMapper.writeValueAsString(requestDtoList), result);
    }

    @Test
    @SneakyThrows
    void getRequestById() {
        when(itemRequestService.getRequestById(any(), any())).thenReturn(itemRequestDto);
        String result = mockMvc.perform(get("/requests/{requestId}", requestId)
                        .header(requestHeaderUserId, userId))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(itemRequestService, times(1)).getRequestById(any(), any());
        assertEquals(objectMapper.writeValueAsString(itemRequestDto), result);
    }
}