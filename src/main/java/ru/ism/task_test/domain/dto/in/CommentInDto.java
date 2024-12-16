package ru.ism.task_test.domain.dto.in;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "Запром на добавление комментария")
public class CommentInDto {
    @NotNull(message = "Поле Id задачи не должно быть пустым")
    @Schema(description = "Id задачи", example = "10")
    private Long taskId;
    @NotBlank(message = "Текст комментания не должен быть пустым")
    @Schema(description = "Текст комментария", example = "хороший комментарий")
    @Size(max = 255, message = "Максимальная длина комментария 255 знаков")
    private String text;

}
