package ru.practicum.shareit.request.response.service;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.response.model.ItemResponseDto;
import ru.practicum.shareit.request.response.model.ItemResponseShotDto;

public interface ItemResponseService {
    ItemResponseDto create(Long requestId, Item item);

    ItemResponseShotDto getByRequestId(Long requestId);
}
