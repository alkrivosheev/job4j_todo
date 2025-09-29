package ru.todo.filter;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Component
@Order(1)
public class AuthorizationFilter extends HttpFilter {

    private static final List<String> PUBLIC_URLS = List.of(
            "/users/login",
            "/users/register",
            "/error"
    );

    private static final List<String> PROTECTED_URL_PATTERNS = List.of(
            "/edit-task",
            "/index",
            "/new-task",
            "/task-detail",
            "/"
    );

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        var uri = request.getRequestURI();
        var contextPath = request.getContextPath();
        var path = uri.substring(contextPath.length());

        if (isPublicUrl(path)) {
            chain.doFilter(request, response);
            return;
        }

        if (isProtectedUrl(path)) {
            var userLoggedIn = request.getSession().getAttribute("user") != null;
            if (!userLoggedIn) {
                handleUnauthorizedAccess(request, response, path);
                return;
            }
        }

        chain.doFilter(request, response);
    }

    private boolean isPublicUrl(String path) {
        return PUBLIC_URLS.stream().anyMatch(path::startsWith);
    }

    private boolean isProtectedUrl(String path) {
        return PROTECTED_URL_PATTERNS.stream().anyMatch(path::startsWith);
    }

    private void handleUnauthorizedAccess(HttpServletRequest request, HttpServletResponse response, String requestedPath)
            throws IOException {

        if (!requestedPath.equals("/") && !isPublicUrl(requestedPath)) {
            request.getSession().setAttribute("redirectAfterLogin", requestedPath);
        }

        request.getSession().setAttribute("authError", "Требуется авторизация");

        var loginPageUrl = request.getContextPath() + "/users/login";
        response.sendRedirect(loginPageUrl);
    }
}