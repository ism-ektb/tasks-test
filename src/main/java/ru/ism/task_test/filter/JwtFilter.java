package ru.ism.task_test.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.ism.task_test.service.JwtService;
import ru.ism.task_test.service.UserService;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    private final UserService userService;

    /**
     * Фильтр JWT, который проверяет JWT-токен в заголовке Authorization
     * и устанавливает аутентификацию пользователя, если токен валиден.
     */
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        // Получаем заголовок Authorization
        String authHeader = request.getHeader("Authorization");

        // Если заголовок не содержит JWT-токена, пропускаем фильтр
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Извлекаем JWT-токен из заголовка
        String token = authHeader.substring(7);

        // Извлекаем имя пользователя из JWT-токена
        String username = jwtService.extractUsername(token);

        // Если имя пользователя не пустое и аутентификация не установлена,
        // проверяем валидность токена и устанавливаем аутентификацию пользователя
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Загружаем детали пользователя
            UserDetails userDetails = userService.loadUserByUsername(username);

            // Проверяем валидность токена для данного пользователя
            if (jwtService.isValid(token, userDetails)) {
                // Создаем объект аутентификации с деталями пользователя
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

                // Устанавливаем детали аутентификации
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                // Устанавливаем аутентификацию
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // Пропускаем фильтр
        filterChain.doFilter(request, response);

    }
}
