package ru.todo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.todo.model.Task;
import ru.todo.service.TaskService;

@Controller
public class NewTaskController {

    private final TaskService taskService;

    public NewTaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/tasks/new")
    public String showCreateForm(Model model) {
        model.addAttribute("task", new Task());
        return "new-task";
    }

    @PostMapping("/tasks")
    public String createTask(@ModelAttribute Task task) {
        taskService.create(task);
        return "redirect:/";
    }
}