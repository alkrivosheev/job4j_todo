package ru.todo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.todo.model.Task;
import ru.todo.service.TaskService;
import java.util.List;
import java.util.Optional;

@Controller
public class IndexController {

    private final TaskService taskService;

    public IndexController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping({"/", "/index"})
    public String index(@RequestParam(value = "filter", required = false) String filter, Model model) {
        List<Task> tasks;

        switch (filter != null ? filter : "all") {
            case "completed":
                tasks = taskService.findCompleted();
                break;
            case "active":
                tasks = taskService.findActive();
                break;
            default:
                tasks = taskService.findAll();
                filter = "all";
        }
        model.addAttribute("tasks", tasks);
        model.addAttribute("filter", filter);
        return "index";
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

    @GetMapping("/tasks/{id}")
    public String getTaskDetail(@PathVariable Integer id, Model model) {
        Optional<Task> taskOptional = taskService.findById(id);

        if (taskOptional.isEmpty()) {
            return "redirect:/?error=Task not found";
        }

        model.addAttribute("task", taskOptional.get());
        return "task-detail";
    }

    @GetMapping("/tasks/{id}/edit")
    public String showEditForm(@PathVariable Integer id, Model model) {
        Task task = taskService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Неверный ID задачи: " + id));
        model.addAttribute("task", task);
        return "edit-task";
    }

    @PostMapping("/tasks/{id}")
    public String updateTask(@PathVariable Integer id, @ModelAttribute Task task) {
        Task existingTask = taskService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Неверный ID задачи: " + id));
        task.setCreated(existingTask.getCreated());
        task.setId(id);

        taskService.update(task);
        return "redirect:/";
    }

    @GetMapping("/tasks/{id}/delete")
    public String deleteTask(@PathVariable Integer id) {
        taskService.delete(id);
        return "redirect:/";
    }

    @PostMapping("/tasks/{id}/toggle")
    public String toggleTaskStatus(@PathVariable Integer id) {
        Optional<Task> taskOptional = taskService.findById(id);

        if (taskOptional.isPresent()) {
            Task task = taskOptional.get();
            task.setDone(!task.isDone());
            taskService.update(task);
        }

        return "redirect:/tasks/" + id;
    }
}
