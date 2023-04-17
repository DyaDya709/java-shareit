package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.model.BookingStatus;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class BookingDto {
    private Long id;
    @NotBlank
    private Long itemId;
    private Long bookerId;
    @NotNull
    private String startDate;
    @NotNull
    private String endDate;
    private BookingStatus status;
}
