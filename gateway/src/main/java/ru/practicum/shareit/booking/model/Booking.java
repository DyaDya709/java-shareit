package ru.practicum.shareit.booking.model;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Getter
@Setter
public class Booking {
    private Long id;
    private User booker;
    private Item item;
    private LocalDateTime start;
    private LocalDateTime end;
    private BookingStatus status;
}