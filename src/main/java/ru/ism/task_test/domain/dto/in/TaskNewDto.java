package ru.ism.task_test.domain.dto.in;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;
import ru.ism.task_test.domain.model.PriorityOfTask;

import java.util.List;

@Schema(description = "Дто для создания задачи администратором")
@Getter
@Builder
@Jacksonized
public class TaskNewDto {
    @Schema(description = "Название задачи", example = "Задача")
    @NotBlank(message = "Название задачи не должно быть пустым")
    @Size(max = 50, message = "Название задачи должен содержать до 50 символов")
    private String title;
    @Schema(description = "Описание задачи", example = "Сложная задача")
    @Size(max = 250, message = "Описание задачи должен содержать до 250 символов")
    private String description;
    @NotNull(message = "поле приоритета не должно быть пустым")
    @Schema(description = "приоритет задачи", type = "enum", example = "LOW")
    private PriorityOfTask priority;
    @Schema(description = "ID исполнителей задачи", type = "array", example = "[1, 2, 3]")
    private List<Long> performersId;
}
