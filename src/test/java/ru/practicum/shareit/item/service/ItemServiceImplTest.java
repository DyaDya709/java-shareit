package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserJpaRepository;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestPropertySource(locations = "classpath:application.properties")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
class ItemServiceImplTest {
    private final UserJpaRepository userJpaRepository;
    private final ItemService itemService;

    @Test
    @Order(1)
    void create() {
        User user = new User();
        user.setId(1L);
        user.setName("user1");
        user.setEmail("user@user.ru");
        userJpaRepository.save(user);
        User user1 = userJpaRepository.findById(1L).orElse(null);
        ItemDto itemDto = ItemDto.builder()
                .name("some item")
                .available(true)
                .description("some description")
                .build();
        Item item = itemService.create(user.getId(), itemDto);
        assertThat(item.getId(), equalTo(1L));
        assertThat(item.getUserId(), equalTo(user.getId()));
        assertThat(item.getDescription(), equalTo("some description"));
    }


    @Test
    @Order(2)
    void update() {
        ItemDto itemDto = ItemDto.builder()
                .name("some item")
                .available(true)
                .description("some update description")
                .build();
        Item item = itemService.update(1L, 1L, itemDto);
        assertThat(item.getDescription(), equalTo("some update description"));
        assertThat(item.getId(), equalTo(1L));
        assertThat(item.getUserId(), equalTo(1L));
    }

    @Test
    @Order(3)
    void get() {
        Item item = itemService.get(1L, 1L);
        assertThat(item.getId(), equalTo(1L));
        assertThat(item.getUserId(), equalTo(1L));
    }

    @Test
    @Order(4)
    void getAll() {
        List<Item> items = itemService.getAll(1L, PageRequest.of(0, 100));
        assertThat(items, hasSize(1));
    }

    @Test
    @Order(5)
    void remove() {
        ItemDto itemDto = ItemDto.builder()
                .name("some item 2")
                .available(true)
                .description("some description 2")
                .build();
        Item item = itemService.create(1L, itemDto);
        List<Item> items = itemService.getAll(1L, PageRequest.of(0, 100));
        assertThat(items, hasSize(2));

        itemService.remove(2L);
        items = itemService.getAll(1L, PageRequest.of(0, 100));
        assertThat(items, hasSize(1));
        assertThat(items.get(0).getId(), equalTo(1L));
    }

    @Test
    @Order(6)
    void findAvailable() {
        List<Item> items = itemService.findAvailable("some update description", PageRequest.of(0, 100));
        assertThat(items, hasSize(1));
        assertThat(items.get(0).getDescription(), equalTo("some update description"));
    }

    @Test
    @Order(7)
    public void createWithInvalidUser() {
        ItemDto itemDto = ItemDto.builder()
                .name("some item 7")
                .available(true)
                .description("some description 7")
                .build();
        assertThrows(NotFoundException.class, () -> itemService.create(99L, itemDto));
    }

    @Test
    @Order(8)
    public void updateWithNullItemId() {
        ItemDto itemDto = ItemDto.builder()
                .name("some item 7")
                .available(true)
                .description("some description 7")
                .build();
        assertThrows(BadRequestException.class, () -> itemService.update(99L, null, itemDto));
    }

    @Test
    @Order(9)
    public void updateWithNullUserId() {
        ItemDto itemDto = ItemDto.builder()
                .name("some item 7")
                .available(true)
                .description("some description 7")
                .build();
        assertThrows(BadRequestException.class, () -> itemService.update(null, 1L, itemDto));
    }

    @Test
    @Order(10)
    public void updateWithNullUserIdAndNullItemId() {
        ItemDto itemDto = ItemDto.builder()
                .name("some item 7")
                .available(true)
                .description("some description 7")
                .build();
        assertThrows(BadRequestException.class, () -> itemService.update(null, null, itemDto));
    }

    @Test
    @Order(10)
    public void updateWithInvalidUserId() {
        ItemDto itemDto = ItemDto.builder()
                .name("some item 7")
                .available(true)
                .description("some description 7")
                .build();
        assertThrows(NotFoundException.class, () -> itemService.update(99L, 1L, itemDto));
    }

    @Test
    @Order(11)
    public void getByInvalidItemId() {
        assertThrows(NotFoundException.class, () -> itemService.get(99L, 99L));
    }

    @Test
    @Order(12)
    public void findAvailableWithEmptyText() {
        List<Item> items = itemService.findAvailable("", PageRequest.of(0, 100));
        assertThat(items.size(), equalTo(0));
    }
}