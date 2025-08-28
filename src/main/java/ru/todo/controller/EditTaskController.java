package ru.todo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.todo.model.Task;
import ru.todo.service.TaskService;

@Controller
public class EditTaskController {

    private final TaskService taskService;

    public EditTaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/tasks/{id}/edit")
    public String showEditForm(@PathVariable Integer id, Model model) {
        Task task = taskService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(String.format("Неверный ID задачи: %d" ,id)));
        model.addAttribute("task", task);
        return "edit-task";
    }

    @PostMapping("/tasks/{id}")
    public String updateTask(@PathVariable Integer id, @ModelAttribute Task task) {
        Task existingTask = taskService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(String.format("Неверный ID задачи: %d" ,id)));
        task.setCreated(existingTask.getCreated());
        task.setId(id);

        taskService.update(task);
        return String.format("redirect:/tasks/%d", id);
    }

    @GetMapping("/tasks/{id}/delete")
    public String deleteTask(@PathVariable Integer id) {
        taskService.delete(id);
        return "redirect:/";
    }
}