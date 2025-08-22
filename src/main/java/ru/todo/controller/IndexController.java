package ru.todo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.todo.model.Task;
import ru.todo.service.TaskService;
import java.util.List;

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
}
