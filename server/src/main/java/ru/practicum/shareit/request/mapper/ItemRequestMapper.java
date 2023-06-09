package ru.practicum.shareit.request.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.response.mapper.ItemResponseMapper;

import java.time.Instant;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ItemRequestMapper {
    public static ItemRequestDto toDto(ItemRequest request) {
        ItemRequestDto dto = new ItemRequestDto();
        dto.setId(request.getId());
        dto.setUserId(request.getUserId());
        dto.setDescription(request.getDescription());
        dto.setCreated(request.getCreated());
        if (request.getResponses() != null) {
            dto.setItemResponseDtos(request.getResponses().stream()
                    .map((ItemResponseMapper::toDto))
                    .collect(Collectors.toSet()));
        }
        return dto;
    }

    public static ItemRequest toEntity(ItemRequestDto requestDto) {
        ItemRequest request = new ItemRequest();
        request.setUserId(requestDto.getUserId());
        request.setDescription(requestDto.getDescription());
        request.setCreated(Instant.now());
        return request;
    }

}
