package ru.practicum.shareit.request.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.shareit.request.response.model.ItemResponse;
import ru.practicum.shareit.request.response.model.ItemResponseDto;

import javax.validation.constraints.NotEmpty;
import java.time.Instant;
import java.util.Set;

/**
 * TODO Sprint add-item-requests.
 */
@NoArgsConstructor
@Getter
@Setter
public class ItemRequestDto {

    private Long id;

    private Long userId;

    @NotEmpty
    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Instant created;

    private Set<ItemResponseDto> itemResponseDtos;

}
