package ru.practicum.shareit.booking.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookingShortDtoImpl implements BookingShortDto {
    private Long id;
    private Long bookerId;

    public BookingShortDtoImpl(Long id, Long bookId) {
        this.id = id;
        this.bookerId = bookId;
    }
}
