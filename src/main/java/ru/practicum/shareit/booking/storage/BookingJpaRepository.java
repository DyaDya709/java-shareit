package ru.practicum.shareit.booking.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingJpaRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByBookerIdAndStatusInAndStartDateBeforeAndEndDateAfterOrderByStartDateAsc(Long bookerId,
                                                                                                   List<BookingStatus> status,
                                                                                                   LocalDateTime startDateTime,
                                                                                                   LocalDateTime endDateTime);

    List<Booking> findAllByBookerIdAndStatusInAndEndDateBeforeOrderByStartDateAsc(Long bookerId,
                                                                                  List<BookingStatus> status,
                                                                                  LocalDateTime dateTime);

    List<Booking> findAllByBookerIdAndStatusInAndStartDateAfterOrderByStartDateAsc(Long bookerId,
                                                                                   List<BookingStatus> status,
                                                                                   LocalDateTime dateTime);

    List<Booking> findAllByBookerIdAndStatusInOrderByStartDateAsc(Long bookerId,
                                                                  List<BookingStatus> status);

    List<Booking> findAllByItemIdInAndStatusInOrderByStartDateAsc(List<Long> itemIds, List<BookingStatus> status);

    List<Booking> findAllByBookerIdOrderByStartDateAsc(Long bookerId);
}
