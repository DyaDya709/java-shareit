package ru.practicum.shareit.request.response.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.request.response.model.ItemResponse;
import ru.practicum.shareit.request.response.model.ItemResponseDto;

import java.util.Optional;

public interface ItemResponseRepository extends JpaRepository<ItemResponse, Long> {
    @Query(value =
            "SELECT * From item_responses WHERE request_id =:requestId",
            nativeQuery = true)
    Optional<ItemResponseDto> findByRequestId(Long requestId);
}
