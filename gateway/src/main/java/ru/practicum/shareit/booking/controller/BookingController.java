package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.client.BookingClient;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.BookingFilter;
import ru.practicum.shareit.exception.BadRequestException;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
public class BookingController {
    private static final String requestHeaderUserId = "X-Sharer-User-Id";
    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> createBooking(@RequestHeader(requestHeaderUserId) Long userId,
                                                @Valid @RequestBody BookingDto bookingDto) {
        return bookingClient.create(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> confirmation(@RequestHeader(requestHeaderUserId) Long userId,
                                               @PathVariable("bookingId") Long bookingId,
                                               @RequestParam Boolean approved) {
        return bookingClient.confirmation(userId, bookingId, approved);
    }

    @GetMapping
    public ResponseEntity<Object> getAllBorrowerBookings(@RequestHeader(requestHeaderUserId) Long userId,
                                                         @RequestParam(required = false,
                                                                 defaultValue = "ALL",
                                                                 value = "state") String filter,
                                                         @RequestParam(required = false) Integer from,
                                                         @RequestParam(required = false) Integer size) {
        validatePageParameters(from, size);
        BookingFilter bookingFilter = getBookingFilter(filter);
        return bookingClient.getAllBorrowerBookings(userId, bookingFilter, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getAllBookingByOwnerItems(@RequestHeader(requestHeaderUserId) Long userId,
                                                            @RequestParam(required = false,
                                                                    defaultValue = "ALL",
                                                                    value = "state") String filter,
                                                            @RequestParam(required = false) Integer from,
                                                            @RequestParam(required = false) Integer size) {

        validatePageParameters(from, size);
        BookingFilter bookingFilter = getBookingFilter(filter);
        return bookingClient.getAllBookingByOwnerItems(userId, bookingFilter, from, size);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getByBookingId(@RequestHeader(requestHeaderUserId) Long userId,
                                                 @PathVariable("bookingId") Long bookingId) {
        return bookingClient.getByBookingId(userId, bookingId);
    }

    private BookingFilter getBookingFilter(String filter) {
        BookingFilter bookingFilter;
        try {
            bookingFilter = BookingFilter.valueOf(filter.toUpperCase());
        } catch (Exception e) {
            throw new BadRequestException("Unknown state: " + filter.toUpperCase());
        }
        return bookingFilter;
    }

    private void validatePageParameters(Integer from, Integer size) {
        if (from != null && from < 0 || size != null && size <= 0) {
            throw new BadRequestException("Invalid page request");
        }
    }
}
