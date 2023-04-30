package ru.practicum.shareit.request.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

@Service
public class ItemRequestServiceImpl implements ItemRequestService {
    @Override
    public ItemRequestDto create(ItemRequest request) {
        return null;
    }

    @Override
    public List<ItemRequestDto> getAllOwnerRequests(ItemRequest request) {
        return null;
    }

    @Override
    public List<ItemRequestDto> getAll(ItemRequest request) {
        return null;
    }
}
