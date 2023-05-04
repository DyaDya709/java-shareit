package ru.practicum.shareit.request.service;

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
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.storage.ItemRequestJpaRepository;
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
public class ItemRequestServiceImplTest {

    private final ItemRequestService itemRequestService;

    private final UserJpaRepository userRepository;

    private final ItemRequestJpaRepository itemRequestJpaRepository;

    private ItemRequestDto itemRequestDto;

    @Test
    @Order(1)
    public void create() {
        User user = new User();
        user.setId(1L);
        user.setName("user1");
        user.setEmail("user@user.ru");
        userRepository.save(user);
        User user1 = userRepository.findById(1L).orElse(null);

        assertThat(user1.getId(), equalTo(1L));

        itemRequestDto = new ItemRequestDto();
        itemRequestDto.setUserId(user.getId());
        itemRequestDto.setDescription("нужна вещь");
        ItemRequestDto itemRequestDtoDb = itemRequestService.create(itemRequestDto);

        assertThat(itemRequestDtoDb.getId(), equalTo(itemRequestDtoDb.getId()));
    }

    @Test
    @Order(1)
    public void createWithInvalidUserId() {
        itemRequestDto = new ItemRequestDto();
        itemRequestDto.setUserId(99L);
        itemRequestDto.setDescription("нужна вещь");
        assertThrows(NotFoundException.class, () -> itemRequestService.create(itemRequestDto));
    }

    @Test
    @Order(2)
    public void getOwnRequests() {
        User user = userRepository.findById(1L).orElse(null);
        assertThat(user.getId(), equalTo(1L));

        ItemRequestDto savedRequest = ItemRequestMapper.toDto(itemRequestJpaRepository.findById(1L).orElse(null));
        List<ItemRequestDto> itemRequestDtosDb = itemRequestService.getOwnRequests(user.getId());

        assertThat(itemRequestDtosDb, hasSize(1));
        assertThat(itemRequestDtosDb.get(0), equalTo(savedRequest));
    }

    @Test
    @Order(2)
    public void getOwnRequestsWithInvalidUserId() {
        assertThrows(NotFoundException.class, () -> itemRequestService.getOwnRequests(99L));
    }

    @Test
    @Order(3)
    public void getOtherRequests() {
        User user2 = new User();
        user2.setId(2L);
        user2.setName("user2");
        user2.setEmail("user2@user.ru");
        userRepository.save(user2);

        User user1 = userRepository.findById(1L).orElse(null);
        assertThat(user1.getId(), equalTo(1L));

        itemRequestDto = new ItemRequestDto();
        itemRequestDto.setUserId(user2.getId());
        itemRequestDto.setDescription("нужна вещь1");
        ItemRequestDto savedRequest2 = itemRequestService.create(itemRequestDto);
        ItemRequestDto savedRequest1 = ItemRequestMapper.toDto(itemRequestJpaRepository.findById(1L).orElse(null));
        List<ItemRequestDto> itemRequestDtosDb = itemRequestService.getOtherRequests(user2.getId(),
                PageRequest.of(0, 1));

        assertThat(itemRequestDtosDb, hasSize(1));
        assertThat(itemRequestDtosDb.get(0), equalTo(savedRequest1));
        assertThat(itemRequestDtosDb.get(0).getUserId(), equalTo(user1.getId()));
    }

    @Test
    @Order(3)
    public void getOtherRequestsWithInvalidUserId() {
        assertThrows(NotFoundException.class, () -> itemRequestService.getOtherRequests(99L, null));
    }

    @Test
    @Order(4)
    public void getRequestById() {
        ItemRequestDto savedRequest2 = itemRequestService.getRequestById(2L, 2L);
        assertThat(savedRequest2.getId(), equalTo(2L));
    }

    @Test
    @Order(4)
    public void getRequestByIdInvalidUserId() {
        assertThrows(NotFoundException.class, () -> itemRequestService.getRequestById(99L, 1L));
    }
}
