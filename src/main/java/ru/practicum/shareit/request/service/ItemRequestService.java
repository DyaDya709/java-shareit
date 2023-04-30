package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public interface ItemRequestService {

    ItemRequestDto create(ItemRequestDto request);

    List<ItemRequestDto> getAllOwnerRequests(ItemRequestDto request);

    List<ItemRequestDto> getAll(ItemRequestDto request);
}
