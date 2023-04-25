package ru.practicum.shareit.item.comment.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.comment.model.Comment;

public interface CommentJpaRepository extends JpaRepository<Comment, Long> {
}
