package ru.practicum.shareit.request.response.service;

import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.response.model.ItemResponse;

public interface ItemResponseService {
    ItemResponse create(Long userId, ItemRequest request);
}
