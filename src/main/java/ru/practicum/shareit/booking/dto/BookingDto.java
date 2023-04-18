package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.model.BookingStatus;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@Data
@Builder
public class BookingDto {
    private Long id;
    @Positive
    private Long itemId;
    private Long bookerId;
    @NotNull
    private String start;
    @NotNull
    private String end;
    private BookingStatus status;
}
