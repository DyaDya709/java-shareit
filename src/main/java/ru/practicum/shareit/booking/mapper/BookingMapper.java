package ru.practicum.shareit.booking.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class BookingMapper {
    private final UserService userService;
    private final ItemService itemService;
    private String patternFormat = "yyyy-MM-dd'T'HH:mm:ss";

    public BookingMapper(UserService userService, ItemService itemService) {
        this.userService = userService;
        this.itemService = itemService;
    }

    public BookingDto toDto(Booking object) {
        return BookingDto.builder()
                .id(object.getId())
                .itemId(object.getItem().getId())
                .bookerId(object.getBooker().getId())
                .start(localDateTimeToString(object.getStart()))
                .end(localDateTimeToString(object.getEnd()))
                .status(object.getStatus())
                .build();
    }

    public Booking toEntity(BookingDto object) {
        Booking booking = new Booking();
        booking.setId(object.getId());
        booking.setItem(itemService.get(object.getItemId(), null));
        booking.setBooker(userService.getUser(object.getBookerId()));
        booking.setStart(stringToLocalDateTime(object.getStart()));
        booking.setEnd(stringToLocalDateTime(object.getEnd()));
        booking.setStatus(object.getStatus());
        return booking;
    }

    public LocalDateTime stringToLocalDateTime(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(patternFormat);
        //.withZone(ZoneId.systemDefault());
        return LocalDateTime.parse(date, formatter);
    }

    public String localDateTimeToString(LocalDateTime date) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(patternFormat);
        //.withZone(ZoneId.systemDefault());
        return date.format(dateTimeFormatter);
    }
}
