package ru.todo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.todo.model.Task;
import ru.todo.service.TaskService;
import java.util.Optional;

@Controller
public class TaskDetailController {

    private final TaskService taskService;

    public TaskDetailController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/tasks/{id}")
    public String getTaskDetail(@PathVariable Integer id, Model model) {
        Optional<Task> taskOptional = taskService.findById(id);

        if (taskOptional.isEmpty()) {
            return "redirect:/?error=Task not found";
        }

        model.addAttribute("task", taskOptional.get());
        return "task-detail";
    }

    @PostMapping("/tasks/{id}/toggle")
    public String toggleTaskStatus(@PathVariable Integer id) {
        Optional<Task> taskOptional = taskService.findById(id);

        if (taskOptional.isPresent()) {
            Task task = taskOptional.get();
            task.setDone(!task.isDone());
            taskService.update(task);
        }
        return String.format("redirect:/tasks/%d", id);
    }
}