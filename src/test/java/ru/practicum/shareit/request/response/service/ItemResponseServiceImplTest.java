package ru.practicum.shareit.request.response.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemJpaRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.response.model.ItemResponseDto;
import ru.practicum.shareit.request.response.model.ItemResponseShotDto;
import ru.practicum.shareit.request.response.storage.ItemResponseRepository;
import ru.practicum.shareit.request.storage.ItemRequestJpaRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserJpaRepository;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@ExtendWith(SpringExtension.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestPropertySource(locations = "classpath:application.properties")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
class ItemResponseServiceImplTest {
    private final ItemResponseService itemResponseService;
    private final ItemJpaRepository itemJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final ItemRequestJpaRepository itemRequestJpaRepository;
    private final ItemResponseRepository itemResponseRepository;
    private ItemRequest request;
    private Item item;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("John");
        user.setEmail("john@gmail.com");
        userJpaRepository.save(user);
        item = new Item();
        item.setId(1L);
        item.setName("item");
        item.setUserId(user.getId());
        item.setAvailable(true);
        item.setDescription("item description");
        itemJpaRepository.save(item);

        request = new ItemRequest();
        request.setUserId(user.getId());
        request.setDescription("request description");
        request.setCreated(Instant.now());
        request.setId(1L);
        itemRequestJpaRepository.save(request);


    }

    @Test
    @Order(1)
    void create() {
        ItemResponseDto itemResponse = itemResponseService.create(request.getId(), item);
        assertNotNull(itemResponse);
    }

    @Test
    @Order(2)
    void createWithInvalidRequestId() {
        assertThrows(NotFoundException.class, () -> itemResponseService.create(99L, item));
    }


    @Test
    @Order(3)
    void getByRequestId() {
        ItemResponseShotDto itemResponse = itemResponseService.getByRequestId(request.getId());
        assertNotNull(itemResponse);
        assertTrue(itemResponse.getId().equals(1L));
    }

    @Test
    @Order(4)
    void getByRequestIdWithInvalidId() {
        assertThrows(NotFoundException.class, () -> itemResponseService.getByRequestId(99L));
    }
}