package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public interface ItemRequestService {

    ItemRequestDto create(ItemRequest request);

    List<ItemRequestDto> getAllOwnerRequests(ItemRequest request);

    List<ItemRequestDto> getAll(ItemRequest request);
}
