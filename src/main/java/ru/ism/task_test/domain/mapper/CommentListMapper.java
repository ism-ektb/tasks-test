package ru.ism.task_test.domain.mapper;

import org.mapstruct.Mapper;
import ru.ism.task_test.domain.dto.out.CommentDto;
import ru.ism.task_test.domain.model.Comment;

import java.util.List;

@Mapper(componentModel = "spring", uses = CommentMapper.class)
public interface CommentListMapper {

    List<CommentDto> modelsToDtos(List<Comment> list);
}
