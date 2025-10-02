package ru.todo.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.todo.model.Task;
import ru.todo.model.User;
import ru.todo.service.TaskService;
import ru.todo.service.UserService;

@Controller
public class NewTaskController {

    private final TaskService taskService;
    private final UserService userService;

    public NewTaskController(TaskService taskService, UserService userService) {
        this.taskService = taskService;
        this.userService = userService;
    }

    @GetMapping("/tasks/new")
    public String showCreateForm(Model model) {
        model.addAttribute("task", new Task());
        return "new-task";
    }

    @PostMapping("/tasks")
    public String createTask(@ModelAttribute Task task, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser != null) {
            task.setUser(currentUser);
        }
        taskService.create(task);
        return "redirect:/";
    }
}