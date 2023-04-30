package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.storage.ItemRequestJpaRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserJpaRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestJpaRepository requestJpaRepository;
    private final UserJpaRepository userRepository;

    @Override
    public ItemRequestDto create(ItemRequestDto request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found"));
        ItemRequest itemRequest = requestJpaRepository.save(ItemRequestMapper.toEntity(request));
        return ItemRequestMapper.toDto(itemRequest);
    }

    @Override
    public List<ItemRequestDto> getAllOwnerRequests(ItemRequestDto request) {
        return null;
    }

    @Override
    public List<ItemRequestDto> getAll(ItemRequestDto request) {
        return requestJpaRepository.findByUserId(request.getUserId()).stream()
                .map(ItemRequestMapper::toDto)
                .collect(Collectors.toList());
    }
}
