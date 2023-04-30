package ru.practicum.shareit.request.response.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ItemResponseDto {
    private Long id;

    private Long itemId;

    @JsonProperty(value = "name")
    private String itemName;

    private Long userId;

    private Long requestId;
}
