package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingFilter;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.BadRequestException;

import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
public class BookingController {
    private static final String REQUEST_HEADER_USER_ID = "X-Sharer-User-Id";
    private final BookingService bookingService;

    @PostMapping
    public Booking createBooking(@RequestHeader(REQUEST_HEADER_USER_ID) long userId,
                                 @RequestBody BookingDto bookingDto) {
        return bookingService.create(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public Booking confirmation(@RequestHeader(REQUEST_HEADER_USER_ID) long userId,
                                @PathVariable("bookingId") long bookingId,
                                @RequestParam Boolean approved) {
        return bookingService.confirmation(userId, bookingId, approved);
    }

    @GetMapping
    public List<Booking> getAllBorrowerBookings(@RequestHeader(REQUEST_HEADER_USER_ID) long userId,
                                                @RequestParam(required = false,
                                                        defaultValue = "ALL",
                                                        value = "state") String filter,
                                                @RequestParam(required = false) Integer from,
                                                @RequestParam(required = false) Integer size) {
        Pageable page = getPage(from, size);
        BookingFilter bookingFilter = getBookingFilter(filter);
        return bookingService.getAllBorrowerBookings(userId, bookingFilter, page);
    }

    @GetMapping("/owner")
    public List<Booking> getAllBookingByOwnerItems(@RequestHeader(REQUEST_HEADER_USER_ID) long userId,
                                                   @RequestParam(required = false,
                                                           defaultValue = "ALL",
                                                           value = "state") String filter,
                                                   @RequestParam(required = false) Integer from,
                                                   @RequestParam(required = false) Integer size) {

        Pageable page = getPage(from, size);
        BookingFilter bookingFilter = getBookingFilter(filter);
        return bookingService.getAllBookingByOwnerItems(userId, bookingFilter, page);
    }

    @GetMapping("/{bookingId}")
    public Booking getByBookingId(@RequestHeader(REQUEST_HEADER_USER_ID) long userId,
                                  @PathVariable("bookingId") long bookingId) {
        return bookingService.getByBookingId(userId, bookingId);
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

    private Pageable getPage(Integer from, Integer size) {
        return PageRequest.of(from == null && size == null ? 0 : from / size, size == null ? Integer.MAX_VALUE : size);
    }
}
