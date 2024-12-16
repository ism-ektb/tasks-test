package ru.ism.task_test.domain.dto.out;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "Dto c данными пользователя")
@Getter
@Builder
public class UserDto {

    @Schema(description = "Номер учетной записи", example = "21")
    private Long id;
    @Schema(description = "Электронная почта", example = "email@emmail.ru")
    private String email;
}
