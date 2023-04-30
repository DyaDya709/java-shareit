package ru.practicum.shareit.request.response.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.request.response.model.ItemResponse;
import ru.practicum.shareit.request.response.model.ItemResponseDto;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ItemResponseMapper {

    public static ItemResponse toEntity(ItemResponseDto itemResponseDto) {
        return null;
    }

    public static ItemResponseDto toDto(ItemResponse itemResponse) {
        ItemResponseDto dto = new ItemResponseDto();
        dto.setId(itemResponse.getId());
        dto.setItemName(itemResponse.getItem().getName());
        dto.setItemId(itemResponse.getItem().getId());
        dto.setUserId(itemResponse.getItem().getUserId());
        dto.setRequestId(itemResponse.getItemRequest().getId());
        return dto;
    }
}
