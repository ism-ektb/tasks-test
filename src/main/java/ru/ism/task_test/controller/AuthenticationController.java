package ru.ism.task_test.controller;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.ism.task_test.domain.dto.in.LoginRequestDto;
import ru.ism.task_test.domain.dto.in.RegistrationRequestDto;
import ru.ism.task_test.domain.dto.out.AuthenticationResponseDto;
import ru.ism.task_test.service.AuthenticationService;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Контроллер регистрации и авторизации")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    /**
     * Регистрация нового пользователя.
     *
     * @param registrationDto данные для регистрации
     * @return ответ о результате регистрации
     */
    @PostMapping("/registration")
    @Schema(description = "Регистрация нового пользователя")
    public AuthenticationResponseDto register(
            @RequestBody @Valid RegistrationRequestDto registrationDto) {
        var response = authenticationService.register(registrationDto);
        log.info("Пользователь с Email= {} зарегистрирован", registrationDto.getEmail());
        return response;
    }

    @PostMapping("/login")
    @Schema(description = "Авторизация пользователя")
    public AuthenticationResponseDto authenticate(
            @RequestBody LoginRequestDto request) {
        var response = authenticationService.authenticate(request);
        log.info("Пользователь с Email= {} прошел аутентификацию", request.getEmail());
        return response;
    }

    @PostMapping("/refresh_token")
    @Schema(description = "Обновление токена")
    public ResponseEntity<AuthenticationResponseDto> refreshToken(
            HttpServletRequest request,
            HttpServletResponse response) {
        return authenticationService.refreshToken(request, response);
    }
}