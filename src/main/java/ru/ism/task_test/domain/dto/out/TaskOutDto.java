package ru.ism.task_test.domain.dto.out;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import ru.ism.task_test.domain.model.PriorityOfTask;
import ru.ism.task_test.domain.model.StatusOfTask;

import java.util.List;

@Getter
@Builder
@Schema(description = "Информация о выполняемой задаче")
@ToString
public class TaskOutDto {
    @Schema(description = "Порядковый номер задвчи")
    private Long id;
    @Schema(description = "Название задачи")
    private String title;
    @Schema(description = "Описание задачи")
    private String description;
    @Schema(description = "Статус выполения задачи", type = "enum")
    private StatusOfTask status;
    @Schema(description = "Приоритет задачи", type = "enum")
    private PriorityOfTask priority;
    @Schema(description = "Комментарии к задаче")
    private List<CommentDto> comments;
    @Schema(description = "Информация о создателе задачи")
    private UserDto author;
    @Schema(description = "Список исполнителей задачи", type = "array")
    private List<UserDto> performers;

}
