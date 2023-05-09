package ru.practicum.shareit.request.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {

    ItemRequestDto create(ItemRequestDto request);

    List<ItemRequestDto> getOwnRequests(Long userId);

    List<ItemRequestDto> getOtherRequests(Long userId, Pageable page);

    ItemRequestDto getRequestById(Long userId, Long requestId);
}
