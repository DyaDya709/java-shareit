package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingFilter;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final String requestHeaderUserId = "X-Sharer-User-Id";
    private final BookingService bookingService;

    @PostMapping
    public Booking createBooking(@RequestHeader(requestHeaderUserId) long userId,
                                 @Valid @RequestBody BookingDto bookingDto) {
        return bookingService.create(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public Booking confirmation(@RequestHeader(requestHeaderUserId) long userId,
                                @PathVariable("bookingId") long bookingId,
                                @RequestParam Boolean approved) {
        return bookingService.confirmation(userId, bookingId, approved);
    }

    @GetMapping
    public List<Booking> getAllBorrowerBookings(@RequestHeader(requestHeaderUserId) long userId,
                                                @RequestParam(required = false,
                                                        defaultValue = "ALL",
                                                        value = "state") BookingFilter filter) {
        return bookingService.getAllBorrowerBookings(userId, filter);
    }

    @GetMapping("/owner")
    public List<Booking> getAllBookingByOwnerItems(@RequestHeader(requestHeaderUserId) long userId,
                                                   @RequestParam(required = false,
                                                           defaultValue = "ALL",
                                                           value = "state") BookingFilter filter) {
        return bookingService.getAllBookingByOwnerItems(userId, filter);
    }

    @GetMapping("/{bookingId}")
    public Booking getByBookingId(@RequestHeader(requestHeaderUserId) long userId,
                                 @PathVariable("bookingId") long bookingId) {
        return bookingService.getByBookingId(userId, bookingId);
    }
}
