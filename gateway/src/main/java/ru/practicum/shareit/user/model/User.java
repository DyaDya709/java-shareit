package ru.practicum.shareit.user.model;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

@Getter
@Setter
public class User {
    private Long id;
    private String email;
    private String name;
    private List<Booking> bookings;

}
