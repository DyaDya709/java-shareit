package ru.practicum.shareit.item.comment.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

class CommentMapperTest {
    private User user;
    private Item item;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("John");
        user.setEmail("john@gmail.com");
        item = new Item();
        item.setId(1L);
        item.setName("item");
        item.setUserId(user.getId());
        item.setAvailable(true);
        item.setDescription("item description");
    }

    @Test
    void toDto() {
        Comment comment = new Comment();
        comment.setItem(item);
        comment.setUserId(user.getId());
        comment.setCreated(LocalDateTime.now());
        comment.setAuthorName(user.getName());
        comment.setText("comment text");
        CommentDto commentDto = CommentMapper.toDto(comment);
        assertThat(commentDto, notNullValue());
    }

    @Test
    void toEntity() {
        CommentDto commentDto = new CommentDto();
        commentDto.setText("comment text");
        Comment comment = CommentMapper.toEntity(commentDto, user, item);
        assertThat(comment, notNullValue());
    }
}