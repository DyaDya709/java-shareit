package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.storage.BookingJpaRepository;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemJpaRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserJpaRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingJpaRepository bookingJpaRepository;
    private final ItemJpaRepository itemJpaRepository;
    private final UserJpaRepository userJpaRepository;

    @Override
    @Transactional
    public Booking create(Long userId, BookingDto bookingDto) {
        User user = userJpaRepository.findById(userId).orElseThrow(() -> new NotFoundException("user not found"));
        Item item = itemJpaRepository.findByIdIs(bookingDto.getItemId());
        if (item == null || !item.getAvailable()) {
            if (item == null) {
                throw new NotFoundException("item not found");
            } else {
                throw new BadRequestException("the item is unavailable");
            }
        }
        if (item.getUserId().equals(user.getId())) {
            throw new NotFoundException("user is the owner of the item");
        }
        bookingDto.setBookerId(userId);
        Booking booking = BookingMapper.toEntity(bookingDto, user, item);
        if (booking.getEnd().isBefore(booking.getStart())) {
            throw new BadRequestException("the end date is before the start date");
        }
        if (booking.getEnd().equals(booking.getStart())) {
            throw new BadRequestException("the end date is equals the start date");
        }
        LocalDateTime now = LocalDateTime.now();
        if (booking.getStart().isBefore(now) || booking.getEnd().isBefore(now)) {
            throw new BadRequestException("the start date is before the end date");
        }
        booking.setStatus(BookingStatus.WAITING);
        Booking bookingDB = bookingJpaRepository.save(booking);
        return bookingDB;
    }

    @Override
    @Transactional
    public Booking confirmation(Long userId, Long bookingId, Boolean approved) {
        Booking booking = bookingJpaRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking not found"));
        if (!booking.getItem().getUserId().equals(userId)) {
            throw new NotFoundException("the user is not the owner of the item");
        }
        if (booking.getStatus().equals(BookingStatus.APPROVED)) {
            throw new BadRequestException("the booking is already approved");
        }
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
                    .orElseThrow(() -> new NotFoundException("the user is not the owner of the item"));
        }
        return booking;
    }

    @Override
    public List<Booking> getAllBorrowerBookings(Long userId, BookingFilter filter, Pageable page) {
        User user = userJpaRepository.findByIdWithBookings(userId)
                .orElseThrow(() -> new NotFoundException("user not found"));
        List<Booking> bookings;
        LocalDateTime now = LocalDateTime.now();
        switch (filter) {
            case CURRENT:
                bookings = user.getBookings().stream()
                        .filter(booking -> booking.getStart().isBefore(now))
                        .filter(booking -> booking.getEnd().isAfter(now))
                        .sorted(Comparator.comparing(Booking::getStart))
                        .skip(page.getPageNumber() * page.getPageSize())
                        .limit(page.getPageSize())
                        .collect(Collectors.toList());
                break;
            case PAST:
                bookings = user.getBookings().stream()
                        .filter(booking -> booking.getEnd().isBefore(now))
                        .sorted(Comparator.comparing(Booking::getStart).reversed())
                        .skip(page.getPageNumber() * page.getPageSize())
                        .limit(page.getPageSize())
                        .collect(Collectors.toList());
                break;
            case FUTURE:
                bookings = user.getBookings().stream()
                        .filter(booking -> booking.getStatus().equals(BookingStatus.APPROVED) ||
                                booking.getStatus().equals(BookingStatus.WAITING))
                        .filter(booking -> booking.getStart().isAfter(now))
                        .sorted(Comparator.comparing(Booking::getStart).reversed())
                        .skip(page.getPageNumber() * page.getPageSize())
                        .limit(page.getPageSize())
                        .collect(Collectors.toList());
                break;
            case WAITING:
                bookings = user.getBookings().stream()
                        .filter(booking -> booking.getStatus().equals(BookingStatus.WAITING))
                        .sorted(Comparator.comparing(Booking::getStart).reversed())
                        .skip(page.getPageNumber() * page.getPageSize())
                        .limit(page.getPageSize())
                        .collect(Collectors.toList());
                break;
            case REJECTED:
                bookings = user.getBookings().stream()
                        .filter(booking -> booking.getStatus().equals(BookingStatus.REJECTED))
                        .sorted(Comparator.comparing(Booking::getStart).reversed())
                        .skip(page.getPageNumber() * page.getPageSize())
                        .limit(page.getPageSize())
                        .collect(Collectors.toList());
                break;
            default:
                List<Booking> bookingDb = user.getBookings();
                int count = bookingDb.size();
                bookings = bookingDb.stream()
                        .sorted(Comparator.comparing(Booking::getStart).reversed())
                        .skip(page.getOffset() > count ? count - 1 : page.getOffset())
                        .limit(page.getPageSize())
                        .collect(Collectors.toList());
                break;
        }
        return bookings;
    }

    @Override
    public List<Booking> getAllBookingByOwnerItems(Long userId, BookingFilter filter, Pageable page) {
        //Ищем вещи владельца вещей
        List<Long> itemIds = itemJpaRepository.findAllByUserId(userId)
                .stream()
                .map(Item::getId)
                .collect(Collectors.toList());
        List<Booking> bookings;
        if (!itemIds.isEmpty()) {
            List<BookingStatus> status = new ArrayList<>();
            LocalDateTime now = LocalDateTime.now();
            switch (filter) {
                case CURRENT:
                    status.clear();
                    status.add(BookingStatus.APPROVED);
                    status.add(BookingStatus.WAITING);
                    status.add(BookingStatus.REJECTED);
                    status.add(BookingStatus.CANCELLED);
                    bookings = bookingJpaRepository
                            .findAllByItemIdInAndStatusInAndStartBeforeAndEndAfterOrderByStartDesc(itemIds, status, now, now,
                                    page);
                    break;
                case PAST:
                    status.clear();
                    status.add(BookingStatus.APPROVED);
                    bookings = bookingJpaRepository
                            .findAllByItemIdInAndStatusInAndEndBeforeOrderByStartDesc(itemIds, status, now, page);
                    break;
                case FUTURE:
                    status.clear();
                    status.add(BookingStatus.APPROVED);
                    status.add(BookingStatus.WAITING);
                    bookings = bookingJpaRepository
                            .findAllByItemIdInAndStatusInAndStartAfterOrderByStartDesc(itemIds, status, now, page);
                    break;
                case WAITING:
                    status.clear();
                    status.add(BookingStatus.WAITING);
                    bookings = bookingJpaRepository
                            .findAllByItemIdInAndStatusInOrderByStartDesc(itemIds, status, page);
                    break;
                case REJECTED:
                    status.clear();
                    status.add(BookingStatus.REJECTED);
                    bookings = bookingJpaRepository
                            .findAllByItemIdInAndStatusInOrderByStartDesc(itemIds, status, page);
                    break;
                default:
                    bookings = bookingJpaRepository.findAllByItemIdInOrderByStartDesc(itemIds, page);
                    break;
            }
            return bookings;
        }
        throw new NotFoundException("No items found");
    }
}
