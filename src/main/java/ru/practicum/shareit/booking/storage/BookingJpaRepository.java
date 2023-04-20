package ru.practicum.shareit.booking.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingJpaRepository extends JpaRepository<Booking, Long> {
    //ищем по itemIds и статусу, дате начала Before и дате конца After (CURRENT)
    List<Booking> findAllByItemIdInAndStatusInAndStartBeforeAndEndAfterOrderByStartDesc(List<Long> itemIds,
                                                                                        List<BookingStatus> status,
                                                                                        LocalDateTime startDateTime,
                                                                                        LocalDateTime endDateTime);

    //ищем по itemIds и статусу и дате начала After (FUTURE)
    List<Booking> findAllByItemIdInAndStatusInAndStartAfterOrderByStartDesc(List<Long> itemIds,
                                                                            List<BookingStatus> status,
                                                                            LocalDateTime startDateTime);

    //ищем по itemIds и статусу и дате конца Before (PAST)
    List<Booking> findAllByItemIdInAndStatusInAndEndBeforeOrderByStartDesc(List<Long> itemIds,
                                                                           List<BookingStatus> status,
                                                                           LocalDateTime endDateTime);

    //ищем по itemId и статусу
    List<Booking> findAllByItemIdInAndStatusInOrderByStartDesc(List<Long> itemIds, List<BookingStatus> status);

    //ищем по itemId
    List<Booking> findAllByItemIdInOrderByStartDesc(List<Long> itemIds);
}
