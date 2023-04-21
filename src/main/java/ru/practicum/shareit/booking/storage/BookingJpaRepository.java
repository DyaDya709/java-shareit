package ru.practicum.shareit.booking.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

    @Query(value =
            "SELECT b.id AS id, b.user_id AS bookerId " +
                    "FROM bookings b " +
                    "WHERE b.item_id = (:itemIds) " +
                    "AND b.start_date IN (SELECT MAX(start_date) AS max_start " +
                    "            FROM bookings " +
                    "            WHERE bookings.start_date < :localDateTime " +
                    "            AND bookings.item_id = (:itemIds) " +
                    "            AND status = 'APPROVED') " +
                    "AND b.status = 'APPROVED' " +
                    "LIMIT 1", nativeQuery = true)
    Optional<BookingShortDto> findLastBookingByItemIds(@Param("itemIds") Long itemIds,
                                                        @Param("localDateTime") LocalDateTime localDateTime);

    @Query(value = "WITH bookings_table AS " +
            "(SELECT MIN(start_date) AS min_start " +
            "FROM bookings " +
            "WHERE start_date > :localDateTime " +
            "AND bookings.item_id = (:itemIds) " +
            "AND status = 'APPROVED') " +
            "SELECT b.id AS id, b.user_id AS bookerId " +
            "FROM bookings b " +
            "JOIN bookings_table bt ON b.start_date = bt.min_start " +
            "WHERE b.item_id = (:itemIds) " +
            "AND b.status = 'APPROVED' " +
            "LIMIT 1", nativeQuery = true)
    Optional<BookingShortDto> findNextBookingByItemIds(@Param("itemIds") Long itemIds,
                                                        @Param("localDateTime") LocalDateTime localDateTime);
}

