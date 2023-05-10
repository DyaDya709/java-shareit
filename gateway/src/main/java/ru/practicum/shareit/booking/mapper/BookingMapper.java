package ru.practicum.shareit.booking.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class BookingMapper {
    private static final String patternFormat = "yyyy-MM-dd'T'HH:mm:ss";

    public static BookingDto toDto(Booking object) {
        return BookingDto.builder()
                .id(object.getId())
                .itemId(object.getItem().getId())
                .bookerId(object.getBooker().getId())
                .start(localDateTimeToString(object.getStart()))
                .end(localDateTimeToString(object.getEnd()))
                .status(object.getStatus())
                .build();
    }

    public static Booking toEntity(BookingDto object, User user, Item item) {
        Booking booking = new Booking();
        booking.setId(object.getId());
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStart(stringToLocalDateTime(object.getStart()));
        booking.setEnd(stringToLocalDateTime(object.getEnd()));
        booking.setStatus(object.getStatus());
        return booking;
    }

    public static LocalDateTime stringToLocalDateTime(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(patternFormat);
        return LocalDateTime.parse(date, formatter);
    }

    public static String localDateTimeToString(LocalDateTime date) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(patternFormat);
        return date.format(dateTimeFormatter);
    }
}
