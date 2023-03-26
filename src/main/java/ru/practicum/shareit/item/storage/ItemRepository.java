package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository {
    Item create(Item item);
    Item update(Item item);
    List<Item> get();
    Item getAll(Long id);
    Boolean remove(Long id);
}
