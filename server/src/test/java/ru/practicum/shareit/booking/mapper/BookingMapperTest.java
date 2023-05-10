package ru.practicum.shareit.booking.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

class BookingMapperTest {
    private User itemOwner;
    private Item item;
    private User booker;

    @BeforeEach
    void setUp() {
        itemOwner = new User();
        itemOwner.setId(1L);
        itemOwner.setName("John");
        itemOwner.setEmail("john@gmail.com");
        item = new Item();
        item.setId(1L);
        item.setName("item");
        item.setUserId(itemOwner.getId());
        item.setAvailable(true);
        item.setDescription("item description");
        booker = new User();
        booker.setId(2L);
        booker.setName("booker");
        booker.setEmail("booker@gmail.com");
    }

    @Test
    void toDto() {
        Booking booking = new Booking();
        booking.setBooker(booker);
        booking.setItem(item);
        booking.setStatus(BookingStatus.WAITING);
        booking.setStart(LocalDateTime.now().plusDays(1));
        booking.setEnd(LocalDateTime.now().plusDays(2));
        BookingDto bookingDto = BookingMapper.toDto(booking);
        assertThat(bookingDto, notNullValue());
    }

    @Test
    void toEntity() {
        BookingDto bookingDto = BookingDto.builder()
                .bookerId(booker.getId())
                .itemId(item.getId())
                .status(BookingStatus.WAITING)
                .start(BookingMapper.localDateTimeToString(LocalDateTime.now()))
                .end(BookingMapper.localDateTimeToString(LocalDateTime.now().plusDays(1)))
                .build();
        Booking booking = BookingMapper.toEntity(bookingDto, booker, item);
        assertThat(booking, notNullValue());
    }

    @Test
    void stringToLocalDateTime() {
        String dateTimeStr = BookingMapper.localDateTimeToString(LocalDateTime.now());
        assertThat(dateTimeStr, notNullValue());
    }

    @Test
    void localDateTimeToString() {
        String dateTimeStr = BookingMapper.localDateTimeToString(LocalDateTime.now());
        LocalDateTime dateTime = BookingMapper.stringToLocalDateTime(dateTimeStr);
        assertThat(dateTime, notNullValue());
    }
}