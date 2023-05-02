package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemJpaRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserJpaRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestPropertySource(locations = "classpath:application.properties")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
class BookingServiceImplTest {
    private final BookingService bookingService;
    private final ItemJpaRepository itemJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private User itemOwner;
    private Item item;
    private User booker;

    @BeforeEach
    void setUp() {
        itemOwner = new User();
        itemOwner.setId(1L);
        itemOwner.setName("John");
        itemOwner.setEmail("john@gmail.com");
        itemOwner = userJpaRepository.save(itemOwner);
        item = new Item();
        item.setId(1L);
        item.setName("item");
        item.setUserId(itemOwner.getId());
        item.setAvailable(true);
        item.setDescription("item description");
        itemJpaRepository.save(item);
        booker = new User();
        booker.setId(2L);
        booker.setName("booker");
        booker.setEmail("booker@gmail.com");
        booker = userJpaRepository.save(booker);
    }

    @Test
    @Order(1)
    void create() {
        BookingDto bookingDto = BookingDto.builder()
                .itemId(1L)
                .start(BookingMapper.localDateTimeToString(LocalDateTime.now().plusDays(1)))
                .end(BookingMapper.localDateTimeToString(LocalDateTime.now().plusDays(5)))
                .status(BookingStatus.WAITING)
                .build();
        Booking booking = bookingService.create(booker.getId(), bookingDto);
        assertThat(booking.getId(), equalTo(1L));
    }

    @Test
    @Order(2)
    void createBookingByOwnerItem() {
        BookingDto bookingDto = BookingDto.builder()
                .itemId(item.getId())
                .start(BookingMapper.localDateTimeToString(LocalDateTime.now().plusDays(1)))
                .end(BookingMapper.localDateTimeToString(LocalDateTime.now().plusDays(5)))
                .status(BookingStatus.WAITING)
                .build();
        assertThrows(NotFoundException.class, () -> bookingService.create(itemOwner.getId(), bookingDto));
    }

    @Test
    @Order(2)
    void createBookingWithStartBeforeNow() {
        BookingDto bookingDto = BookingDto.builder()
                .itemId(item.getId())
                .start(BookingMapper.localDateTimeToString(LocalDateTime.now().minusDays(1)))
                .end(BookingMapper.localDateTimeToString(LocalDateTime.now().plusDays(5)))
                .status(BookingStatus.WAITING)
                .build();
        assertThrows(BadRequestException.class, () -> bookingService.create(booker.getId(), bookingDto));
    }

    @Test
    @Order(2)
    void createBookingWithStartEqualEnd() {
        String start = BookingMapper.localDateTimeToString(LocalDateTime.now().plusDays(1));
        BookingDto bookingDto = BookingDto.builder()
                .itemId(item.getId())
                .start(start)
                .end(start)
                .status(BookingStatus.WAITING)
                .build();
        assertThrows(BadRequestException.class, () -> bookingService.create(booker.getId(), bookingDto));
    }

    @Test
    @Order(2)
    void createBookingWithEndBeforeNow() {
        String end = BookingMapper.localDateTimeToString(LocalDateTime.now().minusDays(1));
        BookingDto bookingDto = BookingDto.builder()
                .itemId(item.getId())
                .start(BookingMapper.localDateTimeToString(LocalDateTime.now().plusDays(1)))
                .end(end)
                .status(BookingStatus.WAITING)
                .build();
        assertThrows(BadRequestException.class, () -> bookingService.create(booker.getId(), bookingDto));
    }

    @Test
    @Order(3)
    void confirmation() {
        Booking booking = bookingService.confirmation(itemOwner.getId(), 1L, true);
        assertTrue(booking.getStatus().equals(BookingStatus.APPROVED));
        assertThat(booking.getBooker().getId(), equalTo(booker.getId()));
    }

    @Test
    @Order(3)
    void confirmationWithInvalidBookingId() {
        assertThrows(NotFoundException.class,
                () -> bookingService.confirmation(itemOwner.getId(), 99L, true));
    }

    @Test
    @Order(3)
    void confirmationWithNotItemOwner() {
        assertThrows(NotFoundException.class,
                () -> bookingService.confirmation(99L, 1L, true));
    }

    @Test
    @Order(4)
    void getByBookingId() {
        Booking booking = bookingService.getByBookingId(booker.getId(), 1L);
        assertThat(booking, notNullValue());
    }

    @Test
    @Order(4)
    void getByBookingWithInvalidId() {
        assertThrows(NotFoundException.class, () -> bookingService.getByBookingId(booker.getId(), 99L));
    }

    @Test
    @Order(4)
    void getByBookingNotOwnerId() {
        assertThrows(NotFoundException.class, () -> bookingService.getByBookingId(3L, 1L));
    }

    @Test
    @Order(5)
    void getAllBorrowerBookings() {
        List<Booking> bookings = bookingService.getAllBorrowerBookings(booker.getId(),
                BookingFilter.ALL,
                PageRequest.of(0, 100));
        assertThat(bookings.size(), equalTo(1));
    }

    @Test
    @Order(5)
    void getCurrentBorrowerBookings() {
        List<Booking> bookings = bookingService.getAllBorrowerBookings(booker.getId(),
                BookingFilter.CURRENT,
                PageRequest.of(0, 100));
        assertThat(bookings.size(), equalTo(0));
    }

    @Test
    @Order(5)
    void getPastBorrowerBookings() {
        List<Booking> bookings = bookingService.getAllBorrowerBookings(booker.getId(),
                BookingFilter.PAST,
                PageRequest.of(0, 100));
        assertThat(bookings.size(), equalTo(0));
    }

    @Test
    @Order(5)
    void getFutureBorrowerBookings() {
        List<Booking> bookings = bookingService.getAllBorrowerBookings(booker.getId(),
                BookingFilter.FUTURE,
                PageRequest.of(0, 100));
        assertThat(bookings.size(), equalTo(1));
    }

    @Test
    @Order(5)
    void getWaitingBorrowerBookings() {
        List<Booking> bookings = bookingService.getAllBorrowerBookings(booker.getId(),
                BookingFilter.WAITING,
                PageRequest.of(0, 100));
        assertThat(bookings.size(), equalTo(0));
    }

    @Test
    @Order(5)
    void getRejectedBorrowerBookings() {
        List<Booking> bookings = bookingService.getAllBorrowerBookings(booker.getId(),
                BookingFilter.REJECTED,
                PageRequest.of(0, 100));
        assertThat(bookings.size(), equalTo(0));
    }

    @Test
    @Order(6)
    void getAllBookingByOwnerItems() {
        List<Booking> bookings = bookingService.getAllBookingByOwnerItems(itemOwner.getId(),
                BookingFilter.ALL,
                PageRequest.of(0, 100));
        assertThat(bookings.size(), equalTo(1));
    }

    @Test
    @Order(6)
    void getCurrentBookingByOwnerItems() {
        List<Booking> bookings = bookingService.getAllBookingByOwnerItems(itemOwner.getId(),
                BookingFilter.CURRENT,
                PageRequest.of(0, 100));
        assertThat(bookings.size(), equalTo(0));
    }

    @Test
    @Order(6)
    void getPastBookingByOwnerItems() {
        List<Booking> bookings = bookingService.getAllBookingByOwnerItems(itemOwner.getId(),
                BookingFilter.PAST,
                PageRequest.of(0, 100));
        assertThat(bookings.size(), equalTo(0));
    }

    @Test
    @Order(6)
    void getFutureBookingByOwnerItems() {
        List<Booking> bookings = bookingService.getAllBookingByOwnerItems(itemOwner.getId(),
                BookingFilter.FUTURE,
                PageRequest.of(0, 100));
        assertThat(bookings.size(), equalTo(1));
    }

    @Test
    @Order(6)
    void getWaitingBookingByOwnerItems() {
        List<Booking> bookings = bookingService.getAllBookingByOwnerItems(itemOwner.getId(),
                BookingFilter.WAITING,
                PageRequest.of(0, 100));
        assertThat(bookings.size(), equalTo(0));
    }

    @Test
    @Order(6)
    void getRejectedBookingByOwnerItems() {
        List<Booking> bookings = bookingService.getAllBookingByOwnerItems(itemOwner.getId(),
                BookingFilter.REJECTED,
                PageRequest.of(0, 100));
        assertThat(bookings.size(), equalTo(0));
    }
}