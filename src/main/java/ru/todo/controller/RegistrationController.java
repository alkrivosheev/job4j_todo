package ru.todo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.todo.model.User;
import ru.todo.service.SimpleUserService;

@Controller
@RequestMapping("/users")
public class RegistrationController {

    private final SimpleUserService userService;

    public RegistrationController(SimpleUserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String showRegistrationForm() {
        return "users/register";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String login,
                               @RequestParam String password,
                               @RequestParam String confirmPassword,
                               RedirectAttributes redirectAttributes) {

        if (login == null || login.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Логин не может быть пустым");
            return "redirect:/users/register";
        }

        if (password == null || password.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Пароль не может быть пустым");
            return "redirect:/users/register";
        }

        if (login.trim().length() < 3 || login.trim().length() > 20) {
            redirectAttributes.addFlashAttribute("errorMessage", "Логин должен быть от 3 до 20 символов");
            return "redirect:/users/register";
        }

        if (password.trim().length() < 6) {
            redirectAttributes.addFlashAttribute("errorMessage", "Пароль должен быть не менее 6 символов");
            return "redirect:/users/register";
        }

        if (!password.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Пароли не совпадают");
            return "redirect:/users/register";
        }

        var existingUser = userService.findByLoginAndPassword(login, password);
        if (existingUser.isPresent()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Пользователь с таким логином уже существует");
            return "redirect:/users/register";
        }

        try {
            User user = new User();
            user.setLogin(login.trim());
            user.setPassword(password.trim());

            User savedUser = userService.create(user);
            redirectAttributes.addFlashAttribute("successMessage", "Регистрация прошла успешно! Теперь вы можете войти.");
            return "redirect:/users/login";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка при регистрации: " + e.getMessage());
            return "redirect:/users/register";
        }
    }
}