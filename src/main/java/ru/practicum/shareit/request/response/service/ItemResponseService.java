package ru.practicum.shareit.request.response.service;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.response.model.ItemResponse;
import ru.practicum.shareit.request.response.model.ItemResponseDto;

public interface ItemResponseService {
    ItemResponseDto create(Long requestId, Item item);
    ItemResponseDto getByRequestId(Long requestId);
}
