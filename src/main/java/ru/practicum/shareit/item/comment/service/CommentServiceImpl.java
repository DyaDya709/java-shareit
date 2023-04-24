package ru.practicum.shareit.item.comment.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.mapper.CommentMapper;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.comment.storage.CommentJpaRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserJpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentJpaRepository commentJpaRepository;
    private final UserJpaRepository userJpaRepository;

    @Override
    @Transactional
    public Comment createComment(Long userId, Long itemId, CommentDto commentDto) {
        User user = userJpaRepository.findByIdWithBookings(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        List<Item> items = user.getBookings().stream()
                .filter(booking -> booking.getStatus().equals(BookingStatus.APPROVED) &&
                        booking.getStart().isBefore(LocalDateTime.now()))
                .map(booking -> booking.getItem()).collect(Collectors.toList());
        Item item = items.stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new BadRequestException("item not found"));

        return commentJpaRepository.save(CommentMapper.toEntity(commentDto, user, item));
    }
}
