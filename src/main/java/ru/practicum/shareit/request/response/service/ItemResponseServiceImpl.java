package ru.practicum.shareit.request.response.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.response.mapper.ItemResponseMapper;
import ru.practicum.shareit.request.response.model.ItemResponse;
import ru.practicum.shareit.request.response.model.ItemResponseDto;
import ru.practicum.shareit.request.response.storage.ItemResponseRepository;
import ru.practicum.shareit.request.storage.ItemRequestJpaRepository;

@Service
@RequiredArgsConstructor
public class ItemResponseServiceImpl implements ItemResponseService {
    private final ItemResponseRepository itemResponseRepository;
    private final ItemRequestJpaRepository itemRequestJpaRepository;

    @Override
    public ItemResponseDto create(Long requestId, Item item) {
        ItemRequest request = itemRequestJpaRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("requestId not found"));
        ItemResponse response = new ItemResponse();
        response.setItem(item);
        response.setItemRequest(request);
        return ItemResponseMapper.toDto(itemResponseRepository.save(response));
    }

    @Override
    public ItemResponseDto getByRequestId(Long requestId) {
        return itemResponseRepository.findByRequestId(requestId)
                .orElseThrow(() -> new NotFoundException("requestId not found"));
    }
}
