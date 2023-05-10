package ru.practicum.shareit.item.mapper;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ItemMapperTest {

    @Test
    void toDto() {
        Item item = new Item();
        item.setName("item1");
        item.setId(1L);
        item.setUserId(1L);
        item.setDescription("description");
        item.setAvailable(true);
        item.setLastBooking(null);
        item.setNextBooking(null);
        ItemDto itemDto = ItemMapper.toDto(item);
        assertNotNull(itemDto);
        assertEquals(1L, itemDto.getId());
    }

    @Test
    void toEntity() {
        ItemDto itemDto = ItemDto.builder()
                .id(1L)
                .name("some item 7")
                .available(true)
                .description("some description 7")
                .build();
        Item item = ItemMapper.toEntity(itemDto);
        assertNotNull(item);
        assertEquals(1L, item.getId());
    }
}