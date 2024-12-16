package ru.ism.task_test.domain.dto.in;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "Запрос на вход в систему существующего пользователя")
public class LoginRequestDto {
    @Schema(description = "Электронная почта", example = "qqq@qqq.qqq")
    @Email(message = "Неверный формат электронной почты")
    @NotBlank(message = "Электронная почта не должна быть пустой")
    private String email;
    @Schema(description = "Пароль", example = "pass")
    @NotBlank(message = "поле Пароль не должно быть пустым")
    private String password;

    @Schema(hidden = true)
    public String getUsername() {
        return email;
    }
}
