package ru.practicum.shareit.booking.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

public interface BookingService {
    Booking create(Long userId, BookingDto bookingDto);

    Booking confirmation(Long userId, Long bookingId, Boolean approved);

    Booking getByBookingId(Long userId, Long bookingId);

    List<Booking> getAllBorrowerBookings(Long userId, BookingFilter filter, Pageable page);

    List<Booking> getAllBookingByOwnerItems(Long userId, BookingFilter filter, Pageable page);

}
