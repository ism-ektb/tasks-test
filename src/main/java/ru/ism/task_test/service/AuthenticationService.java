package ru.ism.task_test.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.ism.task_test.domain.dto.in.LoginRequestDto;
import ru.ism.task_test.domain.dto.in.RegistrationRequestDto;
import ru.ism.task_test.domain.dto.out.AuthenticationResponseDto;
import ru.ism.task_test.domain.mapper.UserMapper;
import ru.ism.task_test.domain.model.User;
import ru.ism.task_test.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;

    private final JwtService jwtService;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;
    private final UserMapper mapper;


    /**
     * Регистрация нового пользователя.
     *
     * @param request запрос на регистрацию
     */
    public AuthenticationResponseDto register(RegistrationRequestDto request) {
        // Создание нового пользователя
        User user = mapper.requestToModel(request);

        // Сохранение пользователя в базе данных
        user = userRepository.save(user); // сохраняем пользователя в базе данных
        String accessToken = jwtService.generateAccessToken(user); // генерируем токен авторизации
        String refreshToken = jwtService.generateRefreshToken(user); // генерируем токен обновления

        // Возвращение объекта с токеном авторизации
        return new AuthenticationResponseDto(accessToken, refreshToken);

    }


    /**
     * Авторизация пользователя.
     *
     * @param request объект с данными пользователя для авторизации
     * @return объект с токеном авторизации
     */
    public AuthenticationResponseDto authenticate(LoginRequestDto request) {
        // Авторизация пользователя
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        // Поиск пользователя по имени пользователя
        User user = userRepository.findByEmail(request.getUsername())
                .orElseThrow();

        String accessToken = jwtService.generateAccessToken(user); // генерируем токен авторизации
        String refreshToken = jwtService.generateRefreshToken(user); // генерируем токен обновления



        // Возвращение объекта с токеном авторизации
        return new AuthenticationResponseDto(accessToken, refreshToken);
    }


    /**
     * Обновляет токен аутентификации.
     *
     * @param request  HTTP-запрос.
     * @param response HTTP-ответ.
     * @return Ответ с обновленным токеном.
     */
    public ResponseEntity<AuthenticationResponseDto> refreshToken(
            HttpServletRequest request,
            HttpServletResponse response) {

        // Получаем заголовок авторизации
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        // Проверяем наличие и формат токена
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // Извлекаем токен из заголовка
        String token = authorizationHeader.substring(7);

        // Извлекаем имя пользователя из токена
        String username = jwtService.extractUsername(token);

        // Находим пользователя по имени
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("No user found"));

        // Проверяем валидность токена обновления
        if (jwtService.isValidRefresh(token, user)) {

            // Генерируем новый доступный токен и обновляемый токен
            String accessToken = jwtService.generateAccessToken(user);
            String refreshToken = jwtService.generateRefreshToken(user);

            // Возвращаем новый ответ с токенами
            return new ResponseEntity<>(new AuthenticationResponseDto(accessToken, refreshToken), HttpStatus.OK);
        }

        // Возвращаем неавторизованный статус
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
