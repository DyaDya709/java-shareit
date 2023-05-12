package ru.practicum.shareit.item.model;

import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.item.comment.model.Comment;

import java.util.List;

@Data
public class Item {
    private Long id;
    private Long userId;
    private String name;
    private String description;
    private Boolean available;
    private List<Comment> comments;
    private BookingShortDto nextBooking;
    private BookingShortDto lastBooking;
    private Long requestId;
}
