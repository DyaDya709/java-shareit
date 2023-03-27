package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * TODO Sprint add-controllers.
 */
@Data
@Builder
public class ItemDto {
    @NotNull
    private String name;
    @NotNull
    private String description;
    @NotNull
    private Boolean available;
}
