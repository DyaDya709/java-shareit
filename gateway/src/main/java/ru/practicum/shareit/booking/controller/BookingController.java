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
    private static final String REQUEST_HEADER_USER_ID = "X-Sharer-User-Id";
    private final BookingClient bookingClient;
    private static final String DEFAULT_FROM = "0";
    private static final String DEFAULT_SIZE = "500";

    @PostMapping
    public ResponseEntity<Object> createBooking(@RequestHeader(REQUEST_HEADER_USER_ID) Long userId,
                                                @Valid @RequestBody BookingDto bookingDto) {
        return bookingClient.create(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> confirmation(@RequestHeader(REQUEST_HEADER_USER_ID) Long userId,
                                               @PathVariable("bookingId") Long bookingId,
                                               @RequestParam Boolean approved) {
        return bookingClient.confirmation(userId, bookingId, approved);
    }

    @GetMapping
    public ResponseEntity<Object> getAllBorrowerBookings(@RequestHeader(REQUEST_HEADER_USER_ID) Long userId,
                                                         @RequestParam(required = false,
                                                                 defaultValue = "ALL",
                                                                 value = "state") String filter,
                                                         @RequestParam(defaultValue = DEFAULT_FROM,
                                                                 required = false) Integer from,
                                                         @RequestParam(defaultValue = DEFAULT_SIZE,
                                                                 required = false) Integer size) {
        validatePageParameters(from, size);
        BookingFilter bookingFilter = getBookingFilter(filter);
        return bookingClient.getAllBorrowerBookings(userId, bookingFilter, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getAllBookingByOwnerItems(@RequestHeader(REQUEST_HEADER_USER_ID) Long userId,
                                                            @RequestParam(required = false,
                                                                    defaultValue = "ALL",
                                                                    value = "state") String filter,
                                                            @RequestParam(defaultValue = DEFAULT_FROM,
                                                                    required = false) Integer from,
                                                            @RequestParam(defaultValue = DEFAULT_SIZE,
                                                                    required = false) Integer size) {

        validatePageParameters(from, size);
        BookingFilter bookingFilter = getBookingFilter(filter);
        return bookingClient.getAllBookingByOwnerItems(userId, bookingFilter, from, size);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getByBookingId(@RequestHeader(REQUEST_HEADER_USER_ID) Long userId,
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
