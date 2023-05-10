package ru.practicum.shareit.item.comment.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.storage.BookingJpaRepository;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemJpaRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserJpaRepository;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
//@TestPropertySource(locations = "classpath:application.properties")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
class CommentServiceImplTest {
    private final CommentService commentService;
    private final UserJpaRepository userJpaRepository;
    private final ItemJpaRepository itemJpaRepository;
    private final BookingJpaRepository bookingJpaRepository;
    private User user;
    private Item item;
    private User booker;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("John");
        user.setEmail("john@gmail.com");
        user = userJpaRepository.save(user);
        item = new Item();
        item.setId(1L);
        item.setName("item");
        item.setUserId(user.getId());
        item.setAvailable(true);
        item.setDescription("item description");
        itemJpaRepository.save(item);
        booker = new User();
        booker.setId(2L);
        booker.setName("booker");
        booker.setEmail("booker@gmail.com");
        booker = userJpaRepository.save(booker);
        BookingDto bookingDto = BookingDto.builder()
                .itemId(1L)
                .start(BookingMapper.localDateTimeToString(LocalDateTime.now().minusDays(1)))
                .end(BookingMapper.localDateTimeToString(LocalDateTime.now().plusDays(5)))
                .status(BookingStatus.APPROVED)
                .build();

        Booking booking = bookingJpaRepository.save(BookingMapper.toEntity(bookingDto, booker, item));
    }

    @Test
    void createComment() {
        CommentDto commentDto = new CommentDto();
        commentDto.setText("comment text");
        Comment comment = commentService.createComment(booker.getId(), item.getId(), commentDto);
        assertThat(comment, notNullValue());
        assertThat(comment.getId(), equalTo(1L));
    }

    @Test
    void createCommentWithInvalidUserId() {
        CommentDto commentDto = new CommentDto();
        commentDto.setText("comment text");
        assertThrows(NotFoundException.class, () -> commentService.createComment(99L, item.getId(), commentDto));
    }

    @Test
    void createCommentWithInvalidItemId() {
        CommentDto commentDto = new CommentDto();
        commentDto.setText("comment text");
        assertThrows(BadRequestException.class, () -> commentService.createComment(booker.getId(), 99L, commentDto));
    }

}