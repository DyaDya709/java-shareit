package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
@Slf4j
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestJpaRepository requestJpaRepository;
    private final UserJpaRepository userRepository;

    @Override
    @Transactional
    public ItemRequestDto create(ItemRequestDto request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found"));
        ItemRequest itemRequest = requestJpaRepository.save(ItemRequestMapper.toEntity(request));
        return ItemRequestMapper.toDto(itemRequest);
    }

    @Override
    public List<ItemRequestDto> getOwnRequests(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        return requestJpaRepository.findByUserId(userId).stream()
                .map(ItemRequestMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemRequestDto> getOtherRequests(Long userId, Pageable page) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        return requestJpaRepository.findByUserIdIsNot(userId, page).stream()
                .map(ItemRequestMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemRequestDto getRequestById(Long userId, Long requestId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        return requestJpaRepository.findById(requestId).map(ItemRequestMapper::toDto)
                .orElseThrow(() -> new NotFoundException("request id not found"));
    }
}
