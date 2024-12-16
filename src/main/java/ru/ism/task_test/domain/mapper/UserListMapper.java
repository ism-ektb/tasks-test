package ru.ism.task_test.domain.mapper;

import org.mapstruct.Mapper;
import ru.ism.task_test.domain.dto.out.UserDto;
import ru.ism.task_test.domain.model.User;

import java.util.List;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface UserListMapper {
    List<UserDto> modelsToDto(List<User> users);

    List<User> longsToModels(List<Long> list);
}
