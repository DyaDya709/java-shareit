package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.storage.BookingJpaRepository;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemJpaRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {
    private final BookingJpaRepository bookingJpaRepository;
    private final ItemJpaRepository itemJpaRepository;
    private final BookingMapper bookingMapper;

    @Override
    @Transactional
    public Booking create(Long userId, BookingDto bookingDto) {
        Item item = itemJpaRepository.findByIdIs(bookingDto.getItemId());
        if (item == null || !item.getAvailable()) {
            throw new BadRequestException("the item is unavailable");
        }
        Booking booking = bookingMapper.toEntity(bookingDto);
        booking.setStatus(BookingStatus.WAITING);
        return bookingJpaRepository.save(booking);
    }

    @Override
    @Transactional
    public Booking confirmation(Long userId, Long bookingId, Boolean approved) {
        Booking booking = bookingJpaRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking not found"));
        List<Item> userItems = itemJpaRepository.findAllByUserId(userId);
        userItems.stream()
                .filter(item -> item.getUserId().equals(userId))
                .map(Item::getId)
                .findFirst().orElseThrow(() -> new BadRequestException("the user is not the owner of the item"));
        booking.setStatus(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        return bookingJpaRepository.save(booking);
    }

    @Override
    public Booking getByBookingId(Long userId, Long bookingId) {
        Booking booking = bookingJpaRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking not found"));
        //Проверим, что бронирование принадлежит заемщику или владельцу вещи
        if (!booking.getBooker().getId().equals(userId)) {
            List<Item> userItems = itemJpaRepository.findAllByUserId(userId);
            userItems.stream()
                    .filter(item -> item.getUserId().equals(userId)).findFirst()
                    .orElseThrow(() -> new ConflictException("the user is not the owner of the item"));
        }
        return booking;
    }

    @Override
    public List<Booking> getAllBorrowerBookings(Long userId, BookingFilter filter) {
        List<Booking> bookings;
        List<BookingStatus> status = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        switch (filter) {
            case CURRENT:
                status.clear();
                status.add(BookingStatus.APPROVED);
                bookings = bookingJpaRepository
                        .findAllByBookerIdAndStatusInAndStartDateBeforeAndEndDateAfterOrderByStartDateAsc(userId, status, now, now);
                break;
            case PAST:
                status.clear();
                status.add(BookingStatus.APPROVED);
                bookings = bookingJpaRepository
                        .findAllByBookerIdAndStatusInAndEndDateBeforeOrderByStartDateAsc(userId, status, now);
                break;
            case FUTURE:
                status.clear();
                status.add(BookingStatus.APPROVED);
                status.add(BookingStatus.WAITING);
                bookings = bookingJpaRepository
                        .findAllByBookerIdAndStatusInAndStartDateAfterOrderByStartDateAsc(userId, status, now);
                break;
            case WAITING:
                status.clear();
                status.add(BookingStatus.WAITING);
                bookings = bookingJpaRepository
                        .findAllByBookerIdAndStatusInOrderByStartDateAsc(userId, status);
                break;
            case REJECTED:
                status.clear();
                status.add(BookingStatus.REJECTED);
                bookings = bookingJpaRepository
                        .findAllByBookerIdAndStatusInOrderByStartDateAsc(userId, status);
                break;
            default:
                bookings = bookingJpaRepository.findAllByBookerIdOrderByStartDateAsc(userId);
                break;
        }
        return bookings;
    }

    @Override
    public List<Booking> getAllBookingByOwnerItems(Long userId, BookingFilter filter) {
        List<Long> itemIds = itemJpaRepository.findAllByUserId(userId)
                .stream()
                .map(Item::getId)
                .collect(Collectors.toList());
        if (itemIds != null) {
            List<BookingStatus> status = new ArrayList<>();
            return bookingJpaRepository.findAllByItemIdInAndStatusInOrderByStartDateAsc(itemIds,status);
        }
        throw new NotFoundException("No items found");
    }
}
