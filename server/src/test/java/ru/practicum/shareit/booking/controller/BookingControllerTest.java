package ru.practicum.shareit.booking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingFilter;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
@AutoConfigureMockMvc
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class BookingControllerTest {
    private static final String requestHeaderUserId = "X-Sharer-User-Id";
    @MockBean
    private final BookingService bookingService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private Booking booking;
    private BookingDto inputBookingDto;
    final Long userId = 1L;
    final Long bookingId = 1L;
    final Pageable pageable = PageRequest.of(0, 2);
    final Pageable pageableDefault = PageRequest.of(0, Integer.MAX_VALUE);

    @BeforeEach
    private void init() {
        final UserDto ownerDto = UserDto.builder()
                .id(1L)
                .name("user1")
                .email("y1@email.ru")
                .build();

        final User booker = new User();
        booker.setId(2L);
        booker.setName("user2");
        booker.setEmail("user2@user.ru");


        final Item item = new Item();
        item.setId(1L);
        item.setName("item1");
        item.setDescription("item1Desc");
        item.setAvailable(true);
        item.setUserId(ownerDto.getId());
        item.setRequestId(2L);

        inputBookingDto = BookingDto.builder()
                .itemId(1L)
                .start("2024-10-23T17:19:33")
                .end("2024-10-23T17:19:45")
                .build();

        booking = new Booking();
        booking.setId(1L);
        booking.setStart(BookingMapper.stringToLocalDateTime("2024-10-23T17:19:33"));
        booking.setEnd(BookingMapper.stringToLocalDateTime("2024-10-23T17:19:45"));
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStatus(BookingStatus.APPROVED);
    }

    @Test
    @SneakyThrows
    void createBooking() {
        when(bookingService.create(userId, inputBookingDto)).thenReturn(booking);
        String result = mockMvc.perform(post("/bookings")
                        .content(objectMapper.writeValueAsString(inputBookingDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(requestHeaderUserId, userId))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(bookingService, times(1)).create(anyLong(), any());
        assertEquals(objectMapper.writeValueAsString(booking), result);
    }

    @Test
    @SneakyThrows
    @Disabled
    void createBookingWithInvalidEnd() {
        inputBookingDto = BookingDto.builder()
                .itemId(1L)
                .start("2024-10-23T17:19:33")
                .end(null)
                .build();
        mockMvc.perform(post("/bookings")
                        .content(objectMapper.writeValueAsString(inputBookingDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(requestHeaderUserId, userId))
                .andExpect(status().is4xxClientError());
        verify(bookingService, never()).create(anyLong(), any());
    }

    @Test
    @SneakyThrows
    @Disabled
    void createBookingWithInvalidStart() {
        inputBookingDto = BookingDto.builder()
                .itemId(1L)
                .start(null)
                .end("2024-10-23T17:19:45")
                .build();
        mockMvc.perform(post("/bookings")
                        .content(objectMapper.writeValueAsString(inputBookingDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(requestHeaderUserId, userId))
                .andExpect(status().is4xxClientError());
        verify(bookingService, never()).create(anyLong(), any());
    }

    @Test
    @SneakyThrows
    @Disabled
    void createBookingWithInvalidItem() {
        inputBookingDto = BookingDto.builder()
                .itemId(-1L)
                .start("2024-10-23T17:19:33")
                .end("2024-10-23T17:19:45")
                .build();
        mockMvc.perform(post("/bookings")
                        .content(objectMapper.writeValueAsString(inputBookingDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(requestHeaderUserId, userId))
                .andExpect(status().is4xxClientError());
        verify(bookingService, never()).create(anyLong(), any());
    }

    @Test
    @SneakyThrows
    void confirmation() {
        when(bookingService.confirmation(bookingId, userId, true)).thenReturn(booking);
        String result = mockMvc.perform(patch("/bookings/{bookingId}", bookingId)
                        .param("approved", "true")
                        .header(requestHeaderUserId, userId))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(bookingService, times(1)).confirmation(anyLong(), anyLong(), anyBoolean());
        assertEquals(objectMapper.writeValueAsString(booking), result);
    }

    @Test
    @SneakyThrows
    void confirmationWithInvalidApprove() {
        mockMvc.perform(patch("/bookings/{bookingId}", bookingId)
                        .header(requestHeaderUserId, userId))
                .andExpect(status().is4xxClientError());
        verify(bookingService, never()).confirmation(anyLong(), anyLong(), anyBoolean());
    }

    @Test
    @SneakyThrows
    void confirmationWithNotUserId() {
        mockMvc.perform(patch("/bookings/{bookingId}", bookingId)
                        .param("approved", "true"))
                .andExpect(status().is4xxClientError());
        verify(bookingService, never()).confirmation(anyLong(), anyLong(), anyBoolean());
    }

    @Test
    @SneakyThrows
    void confirmationWithNotBookingId() {
        mockMvc.perform(patch("/bookings/{bookingId}", "")
                        .param("approved", "true"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @SneakyThrows
    void getByBookingId() {
        when(bookingService.getByBookingId(userId, bookingId)).thenReturn(booking);
        String result = mockMvc.perform(get("/bookings/{bookingId}", bookingId)
                        .header(requestHeaderUserId, userId))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(bookingService, times(1)).getByBookingId(anyLong(), anyLong());
        assertEquals(objectMapper.writeValueAsString(booking), result);
    }

    @Test
    @SneakyThrows
    void getByBookingIdWithInvalidUserId() {
        mockMvc.perform(get("/bookings/{bookingId}", bookingId))
                .andExpect(status().is4xxClientError());
        verify(bookingService, never()).getByBookingId(anyLong(), anyLong());
    }

    @Test
    @SneakyThrows
    void getByBookingIdWithInvalidBookingId() {
        mockMvc.perform(get("/bookings/{bookingId}", "e")
                        .header(requestHeaderUserId, userId))
                .andExpect(status().is4xxClientError());
        verify(bookingService, never()).getByBookingId(anyLong(), anyLong());
    }

    @Test
    @SneakyThrows
    void getAllBorrowerBookings() {
        final List<Booking> bookings = Arrays.asList(booking);
        when(bookingService.getAllBorrowerBookings(userId, BookingFilter.ALL, pageable)).thenReturn(bookings);
        String result = mockMvc.perform(get("/bookings")
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "2")
                        .header(requestHeaderUserId, userId))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(bookingService, times(1)).getAllBorrowerBookings(userId, BookingFilter.ALL, pageable);
        assertEquals(objectMapper.writeValueAsString(bookings), result);
    }

    @Test
    @SneakyThrows
    void getAllBorrowerBookingsNullPageParam() {
        final List<Booking> bookings = Arrays.asList(booking);
        when(bookingService.getAllBorrowerBookings(userId, BookingFilter.ALL, pageableDefault)).thenReturn(bookings);
        String result = mockMvc.perform(get("/bookings")
                        .param("state", "ALL")
                        .header(requestHeaderUserId, userId))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(bookingService, times(1)).getAllBorrowerBookings(userId, BookingFilter.ALL, pageableDefault);
        assertEquals(objectMapper.writeValueAsString(bookings), result);
    }

    @Test
    @SneakyThrows
    void getAllBorrowerBookingsPast() {
        final List<Booking> bookings = Arrays.asList(booking);
        when(bookingService.getAllBorrowerBookings(userId, BookingFilter.PAST, pageable)).thenReturn(bookings);
        String result = mockMvc.perform(get("/bookings")
                        .param("state", "PAST")
                        .param("from", "0")
                        .param("size", "2")
                        .header(requestHeaderUserId, userId))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(bookingService, times(1)).getAllBorrowerBookings(userId, BookingFilter.PAST, pageable);
        assertEquals(objectMapper.writeValueAsString(bookings), result);
    }

    @Test
    @SneakyThrows
    void getAllBorrowerBookingsPastNullPageParam() {
        final List<Booking> bookings = Arrays.asList(booking);
        when(bookingService.getAllBorrowerBookings(userId, BookingFilter.PAST, pageableDefault)).thenReturn(bookings);
        String result = mockMvc.perform(get("/bookings")
                        .param("state", "PAST")
                        .header(requestHeaderUserId, userId))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(bookingService, times(1)).getAllBorrowerBookings(userId, BookingFilter.PAST, pageableDefault);
        assertEquals(objectMapper.writeValueAsString(bookings), result);
    }

    @Test
    @SneakyThrows
    void getAllBorrowerBookingsWaiting() {
        final List<Booking> bookings = Arrays.asList(booking);
        when(bookingService.getAllBorrowerBookings(userId, BookingFilter.WAITING, pageable)).thenReturn(bookings);
        String result = mockMvc.perform(get("/bookings")
                        .param("state", "WAITING")
                        .param("from", "0")
                        .param("size", "2")
                        .header(requestHeaderUserId, userId))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(bookingService, times(1)).getAllBorrowerBookings(userId, BookingFilter.WAITING, pageable);
        assertEquals(objectMapper.writeValueAsString(bookings), result);
    }

    @Test
    @SneakyThrows
    void getAllBorrowerBookingsWaitingNullPageParam() {
        final List<Booking> bookings = Arrays.asList(booking);
        when(bookingService.getAllBorrowerBookings(userId, BookingFilter.WAITING, pageableDefault)).thenReturn(bookings);
        String result = mockMvc.perform(get("/bookings")
                        .param("state", "WAITING")
                        .header(requestHeaderUserId, userId))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(bookingService, times(1)).getAllBorrowerBookings(userId, BookingFilter.WAITING, pageableDefault);
        assertEquals(objectMapper.writeValueAsString(bookings), result);
    }

    @Test
    @SneakyThrows
    void getAllBorrowerBookingsCurrent() {
        final List<Booking> bookings = Arrays.asList(booking);
        when(bookingService.getAllBorrowerBookings(userId, BookingFilter.CURRENT, pageable)).thenReturn(bookings);
        String result = mockMvc.perform(get("/bookings")
                        .param("state", "CURRENT")
                        .param("from", "0")
                        .param("size", "2")
                        .header(requestHeaderUserId, userId))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(bookingService, times(1)).getAllBorrowerBookings(userId, BookingFilter.CURRENT, pageable);
        assertEquals(objectMapper.writeValueAsString(bookings), result);
    }

    @Test
    @SneakyThrows
    void getAllBorrowerBookingsCurrentNullPageParam() {
        final List<Booking> bookings = Arrays.asList(booking);
        when(bookingService.getAllBorrowerBookings(userId, BookingFilter.CURRENT, pageableDefault)).thenReturn(bookings);
        String result = mockMvc.perform(get("/bookings")
                        .param("state", "CURRENT")
                        .header(requestHeaderUserId, userId))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(bookingService, times(1)).getAllBorrowerBookings(userId, BookingFilter.CURRENT, pageableDefault);
        assertEquals(objectMapper.writeValueAsString(bookings), result);
    }

    @Test
    @SneakyThrows
    void getAllBorrowerBookingsFuture() {
        final List<Booking> bookings = Arrays.asList(booking);
        when(bookingService.getAllBorrowerBookings(userId, BookingFilter.FUTURE, pageable)).thenReturn(bookings);
        String result = mockMvc.perform(get("/bookings")
                        .param("state", "FUTURE")
                        .param("from", "0")
                        .param("size", "2")
                        .header(requestHeaderUserId, userId))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(bookingService, times(1)).getAllBorrowerBookings(userId, BookingFilter.FUTURE, pageable);
        assertEquals(objectMapper.writeValueAsString(bookings), result);
    }

    @Test
    @SneakyThrows
    void getAllBorrowerBookingsFutureNullPageParam() {
        final List<Booking> bookings = Arrays.asList(booking);
        when(bookingService.getAllBorrowerBookings(userId, BookingFilter.FUTURE, pageableDefault)).thenReturn(bookings);
        String result = mockMvc.perform(get("/bookings")
                        .param("state", "FUTURE")
                        .header(requestHeaderUserId, userId))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(bookingService, times(1)).getAllBorrowerBookings(userId, BookingFilter.FUTURE,
                pageableDefault);
        assertEquals(objectMapper.writeValueAsString(bookings), result);
    }

    @Test
    @SneakyThrows
    void getAllBorrowerBookingsRejected() {
        final List<Booking> bookings = Arrays.asList(booking);
        when(bookingService.getAllBorrowerBookings(userId, BookingFilter.REJECTED, pageable)).thenReturn(bookings);
        String result = mockMvc.perform(get("/bookings")
                        .param("state", "REJECTED")
                        .param("from", "0")
                        .param("size", "2")
                        .header(requestHeaderUserId, userId))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(bookingService, times(1)).getAllBorrowerBookings(userId, BookingFilter.REJECTED, pageable);
        assertEquals(objectMapper.writeValueAsString(bookings), result);
    }

    @Test
    @SneakyThrows
    void getAllBorrowerBookingsRejectedNullPageParam() {
        final List<Booking> bookings = Arrays.asList(booking);
        when(bookingService.getAllBorrowerBookings(userId, BookingFilter.REJECTED, pageableDefault)).thenReturn(bookings);
        String result = mockMvc.perform(get("/bookings")
                        .param("state", "REJECTED")
                        .header(requestHeaderUserId, userId))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(bookingService, times(1)).getAllBorrowerBookings(userId, BookingFilter.REJECTED,
                pageableDefault);
        assertEquals(objectMapper.writeValueAsString(bookings), result);
    }

    @Test
    @SneakyThrows
    void getAllBorrowerBookingsUnsupportedStatus() {
        mockMvc.perform(get("/bookings")
                        .param("state", "UNSUPPORTED_STATUS")
                        .param("from", "0")
                        .param("size", "2")
                        .header(requestHeaderUserId, userId))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.error", is("Unknown state: UNSUPPORTED_STATUS")));
    }

    @Test
    @SneakyThrows
    void getAllBookingByOwnerItems() {
        final List<Booking> bookingDtoList = Arrays.asList(booking);
        final Pageable pageableSize = PageRequest.of(0, 2);
        when(bookingService.getAllBookingByOwnerItems(userId, BookingFilter.PAST, pageableSize))
                .thenReturn(bookingDtoList);
        String result = mockMvc.perform(get("/bookings/owner")
                        .param("state", "PAST")
                        .param("from", "0")
                        .param("size", "2")
                        .header(requestHeaderUserId, userId))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(bookingService, times(1))
                .getAllBookingByOwnerItems(userId, BookingFilter.PAST, pageableSize);
        assertEquals(objectMapper.writeValueAsString(bookingDtoList), result);
    }

    @Test
    @SneakyThrows
    void getAllBookingByOwnerItemsFuture() {
        final List<Booking> bookingDtoList = Arrays.asList(booking);
        final Pageable pageableSize = PageRequest.of(0, 2);
        when(bookingService.getAllBookingByOwnerItems(userId, BookingFilter.FUTURE, pageableSize))
                .thenReturn(bookingDtoList);
        String result = mockMvc.perform(get("/bookings/owner")
                        .param("state", "FUTURE")
                        .param("from", "0")
                        .param("size", "2")
                        .header(requestHeaderUserId, userId))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(bookingService, times(1))
                .getAllBookingByOwnerItems(userId, BookingFilter.FUTURE, pageableSize);
        assertEquals(objectMapper.writeValueAsString(bookingDtoList), result);
    }

    @Test
    @SneakyThrows
    void getAllBookingByOwnerItemsCurrent() {
        final List<Booking> bookingDtoList = Arrays.asList(booking);
        final Pageable pageableSize = PageRequest.of(0, 2);
        when(bookingService.getAllBookingByOwnerItems(userId, BookingFilter.CURRENT, pageableSize))
                .thenReturn(bookingDtoList);
        String result = mockMvc.perform(get("/bookings/owner")
                        .param("state", "CURRENT")
                        .param("from", "0")
                        .param("size", "2")
                        .header(requestHeaderUserId, userId))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(bookingService, times(1))
                .getAllBookingByOwnerItems(userId, BookingFilter.CURRENT, pageableSize);
        assertEquals(objectMapper.writeValueAsString(bookingDtoList), result);
    }

    @Test
    @SneakyThrows
    void getAllBookingByOwnerItemsUnsupported() {
        final Pageable pageableSize = PageRequest.of(0, 2);
        mockMvc.perform(get("/bookings/owner")
                        .param("state", "UNSUPPORTED_STATUS")
                        .param("from", "0")
                        .param("size", "2")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.error", is("Unknown state: UNSUPPORTED_STATUS")));
    }

    @Test
    @SneakyThrows
    void getAllBookingByOwnerItemsWithInvalidUserId() {
        final Pageable pageableSize = PageRequest.of(0, 2);
        mockMvc.perform(get("/bookings/owner")
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "2"))
                .andExpect(status().is4xxClientError());
        verify(bookingService, never()).getAllBookingByOwnerItems(userId, BookingFilter.ALL, pageableSize);
    }
}