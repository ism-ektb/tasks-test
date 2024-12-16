package ru.ism.task_test.domain.dto.out;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "дто для публикации комментария")
public class CommentDto {
    @Schema(description = "Id комментария")
    private Long id;
    @Schema(description = "Информаци о пользователе оставившем комментаний")
    private UserDto commentAuthor;
    @Schema(description = "Текст комментария")
    private String text;
}
