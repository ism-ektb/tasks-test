package ru.ism.task_test.domain.dto.in;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import ru.ism.task_test.domain.model.PriorityOfTask;
import ru.ism.task_test.domain.model.StatusOfTask;

import java.util.List;

@Getter
@Builder
@Schema(description = "")
public class TaskPatchAdminDto {
    @Schema(description = "Название задачи", example = "Задача")
    @Size(max = 50, message = "Название задачи должен содержать до 50 символов")
    private String title;
    @Schema(description = "Описание задачи", example = "Сложная задача")
    @Size(max = 250, message = "Описание задачи должен содержать до 250 символов")
    private String description;
    @Schema(description = "Статус выполнения задачи", type = "enum", example = "IN_PROGRESS")
    private StatusOfTask status;
    @Schema(description = "приоритет задачи", type = "enum", example = "LOW")
    private PriorityOfTask priority;
    @Schema(description = "ID исполнителей задачи", type = "array", example = "[1, 2, 3]")
    private List<Long> performersId;
}
