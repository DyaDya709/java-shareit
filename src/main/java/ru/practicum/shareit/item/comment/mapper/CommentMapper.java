package ru.practicum.shareit.item.comment.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Component
public class CommentMapper {
    public CommentDto toDto(Comment comment) {
        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setText(comment.getText());
        commentDto.setAuthor(comment.getAuthorName());
        commentDto.setCreated(comment.getCreated());
        return commentDto;
    }

    public Comment toEntity(CommentDto commentDto, User user, Item item) {
        Comment comment = new Comment();
        comment.setUserId(user.getId());
        comment.setText(commentDto.getText());
        comment.setAuthorName(user.getName());
        comment.setCreated(LocalDateTime.now());
        comment.setItem(item);
        return comment;
    }
}
