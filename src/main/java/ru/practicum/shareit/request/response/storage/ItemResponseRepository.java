package ru.practicum.shareit.request.response.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.request.response.model.ItemResponse;
import ru.practicum.shareit.request.response.model.ItemResponseShotDto;

import java.util.Optional;

public interface ItemResponseRepository extends JpaRepository<ItemResponse, Long> {
    @Query(value =
            "SELECT id AS id, " +
                    "item_id AS itemId, " +
                    "request_id AS requestId " +
                    "From item_responses " +
                    "WHERE request_id =:requestId " +
                    "LIMIT 1", nativeQuery = true)
    Optional<ItemResponseShotDto> findByRequestId(Long requestId);

}
