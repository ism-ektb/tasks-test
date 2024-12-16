package ru.ism.task_test.domain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.ism.task_test.domain.dto.in.TaskNewDto;
import ru.ism.task_test.domain.dto.out.TaskOutDto;
import ru.ism.task_test.domain.model.Task;

@Mapper(componentModel = "spring", uses = {UserListMapper.class, CommentListMapper.class})
public interface TaskMapper {
    @Mapping(target = "performers", source = "taskNewDto.performersId")
    Task dtoToModels(TaskNewDto taskNewDto);

    TaskOutDto modelToDto(Task task);

}
