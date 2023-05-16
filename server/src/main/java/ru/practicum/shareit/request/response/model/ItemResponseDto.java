package ru.practicum.shareit.request.response.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ItemResponseDto implements ItemResponseShotDto {
    @JsonIgnore
    private Long id;

    @JsonProperty(value = "id")
    private Long itemId;

    @JsonProperty(value = "name")
    private String itemName;

    private String description;

    private Boolean available;

    private Long userId;

    private Long requestId;
}
