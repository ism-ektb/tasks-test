package ru.ism.task_test.domain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.ism.task_test.domain.dto.in.CommentInDto;
import ru.ism.task_test.domain.dto.out.CommentDto;
import ru.ism.task_test.domain.model.Comment;

@Mapper(componentModel = "spring", uses = {UserMapper.class, TaskLongMapper.class})
public interface CommentMapper {
    @Mapping(target = "commentAuthor", source = "author")
    CommentDto modelsToDto(Comment comment);

    @Mapping(target = "task", source = "comment.taskId")
    Comment dtoToModels(CommentInDto comment);
}
