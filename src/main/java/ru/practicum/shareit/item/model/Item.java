package ru.practicum.shareit.item.model;

import lombok.Builder;
import lombok.Data;

/**
 * TODO Sprint add-controllers.
 */
@Data
@Builder
public class Item {
    private long id;
    private Long ownerId;
    private String name;
    private String description;
    private Boolean available;
}
