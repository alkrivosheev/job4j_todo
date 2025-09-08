package ru.todo.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.todo.service.SimpleUserService;

@Controller
@RequestMapping("/users")
public class AuthController {

    private final SimpleUserService userService;

    public AuthController(SimpleUserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String showLoginForm(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            return "redirect:/";
        }
        return "users/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String login,
                        @RequestParam String password,
                        HttpServletRequest request,
                        RedirectAttributes redirectAttributes) {

        if (login == null || login.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Логин не может быть пустым");
            return "redirect:/users/login";
        }

        if (password == null || password.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Пароль не может быть пустым");
            return "redirect:/users/login";
        }

        var userOptional = userService.findByLoginAndPassword(login.trim(), password.trim());

        if (userOptional.isPresent()) {
            var user = userOptional.get();
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            session.setAttribute("userId", user.getId());
            session.setAttribute("username", user.getLogin());

            String redirectUrl = (String) session.getAttribute("redirectAfterLogin");
            if (redirectUrl != null && !redirectUrl.isEmpty() && !redirectUrl.equals("/")) {
                session.removeAttribute("redirectAfterLogin");
                return "redirect:" + redirectUrl;
            }
            return "redirect:/";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Неверный логин или пароль");
            return "redirect:/users/login";
        }
    }
}