package ru.practicum.shareit.item.model;

import lombok.Builder;
import lombok.Data;

/**
 * TODO Sprint add-controllers.
 */
@Data
@Builder
public class Item {
    long id;
    Long ownerId;
    String name;
    String description;
    Boolean isAvailable;
}
