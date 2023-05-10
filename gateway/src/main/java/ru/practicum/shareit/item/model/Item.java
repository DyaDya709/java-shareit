package ru.practicum.shareit.item.model;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.item.comment.model.Comment;

import java.util.List;

@Getter
@Setter
public class Item {
    private Long id;
    private Long userId;
    private String name;
    private String description;
    private Boolean available;
    private List<Comment> comments;
    BookingShortDto nextBooking;
    BookingShortDto lastBooking;
    Long requestId;
}
