package ru.ism.task_test.domain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.ism.task_test.domain.dto.in.LoginRequestDto;
import ru.ism.task_test.domain.dto.in.RegistrationRequestDto;
import ru.ism.task_test.domain.dto.out.UserDto;
import ru.ism.task_test.domain.model.User;

@Mapper(componentModel = "spring", uses = PasswordEncoderMapper.class)
public interface UserMapper {
    @Mapping(source = "password", target = "password", qualifiedBy = EncodedMapping.class)
    User requestToModel(RegistrationRequestDto dto);

    @Mapping(source = "password", target = "password", qualifiedBy = EncodedMapping.class)
    User requestToModel(LoginRequestDto dto);

    UserDto modelToDto(User user);

    @Mapping(source = "id", target = "id")
    User longToModel(Long id);
}
