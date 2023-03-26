package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.sql.ResultSet;

public class ItemMapper {
    public static Item makeItem(ResultSet rs) {
        return Item.builder().build();
    }
    public static Item makeItem(ItemDto itemDto) {
        return Item.builder().build();
    }
    public static ItemDto makeItemDto(ResultSet rs) {
        return ItemDto.builder().build();
    }
    public static ItemDto makeItemDto(Item item) {
        return ItemDto.builder().build();
    }
}
