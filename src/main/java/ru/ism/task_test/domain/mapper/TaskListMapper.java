package ru.ism.task_test.domain.mapper;

import org.mapstruct.Mapper;
import ru.ism.task_test.domain.dto.out.TaskOutDto;
import ru.ism.task_test.domain.model.Task;

import java.util.List;

@Mapper(componentModel = "spring", uses = TaskMapper.class)
public interface TaskListMapper {
    List<TaskOutDto> modelsToDtos(List<Task> list);
}
