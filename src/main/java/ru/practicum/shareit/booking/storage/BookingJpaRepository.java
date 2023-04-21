package ru.practicum.shareit.booking.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.booking.dto.BookingShortDto;
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

    @Query("SELECT b FROM Booking b JOIN FETCH b.item i WHERE i.id IN :itemIds AND b.status = :status")
    List<Booking> findByItemIdsAndStatusWithItems(@Param("itemIds") List<Long> itemIds,
                                                  @Param("status") BookingStatus status);

    @Query("SELECT NEW ru.practicum.shareit.booking.dto.BookingShortDtoImpl(b.id, b.booker.id) " +
            "FROM Booking b " +
            "WHERE b.item.id IN :itemIds " +
            "AND b.start > :now " +
            "AND b.status = 'APPROVED' " +
            "ORDER BY b.start ASC")
    List<BookingShortDto> findNextBookingByItemIds(@Param("itemIds") List<Long> itemIds, LocalDateTime now);

    @Query("SELECT NEW ru.practicum.shareit.booking.dto.BookingShortDtoImpl(b.id, b.booker.id) " +
            "FROM Booking b " +
            "WHERE b.item.id IN :itemIds " +
            "AND b.start < :now " +
            "AND b.status = 'APPROVED' " +
            "ORDER BY b.start DESC ")
    List<BookingShortDto> findLastBookingByItemIds(@Param("itemIds") List<Long> itemIds, LocalDateTime now);
}

