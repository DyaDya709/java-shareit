package ru.practicum.shareit.booking.mapper;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Service
public class BookingMapper {
    private final UserService userService;
    private final ItemService itemService;
    private String PATTERN_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";

    public BookingMapper(UserService userService, ItemService itemService) {
        this.userService = userService;
        this.itemService = itemService;
    }

    public BookingDto toDto(Booking object) {
        return BookingDto.builder()
                .id(object.getId())
                .itemId(object.getItem().getId())
                .bookerId(object.getBooker().getId())
                .startDate(localDateTimeToString(object.getStartDate()))
                .endDate(localDateTimeToString(object.getEndDate()))
                .status(object.getStatus())
                .build();
    }

    public Booking toEntity(BookingDto object) {
        Booking booking = new Booking();
        booking.setId(object.getId());
        booking.setItem(itemService.get(object.getItemId()));
        booking.setBooker(userService.getUser(object.getBookerId()));
        booking.setStartDate(stringToLocalDateTime(object.getStartDate()));
        booking.setEndDate(stringToLocalDateTime(object.getEndDate()));
        booking.setStatus(object.getStatus());
        return booking;
    }

    public LocalDateTime stringToLocalDateTime(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(PATTERN_FORMAT)
                .withZone(ZoneId.systemDefault());
        return LocalDateTime.parse(date, formatter);
    }

    public String localDateTimeToString(LocalDateTime date) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(PATTERN_FORMAT)
                .withZone(ZoneId.systemDefault());
        return date.format(dateTimeFormatter);
    }
}
