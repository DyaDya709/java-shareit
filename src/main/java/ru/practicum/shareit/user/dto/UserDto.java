package ru.practicum.shareit.user.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class UserDto {
    private Long id;
    @Email(message = "invalid email address")
    @NotNull
    private String email;
    private String name;

    @JsonCreator //нужен для десериализации в тестах
    public static UserDto fromJson(@JsonProperty("id") Long id,
                                   @JsonProperty("email") String email,
                                   @JsonProperty("name") String name) {
        return UserDto.builder()
                .id(id)
                .email(email)
                .name(name)
                .build();
    }
}
