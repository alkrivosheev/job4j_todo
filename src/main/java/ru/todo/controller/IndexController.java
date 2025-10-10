package ru.todo.controller;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.todo.dto.TaskDto;
import ru.todo.model.Task;
import ru.todo.model.User;
import ru.todo.service.TaskService;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
public class IndexController {

    private final TaskService taskService;

    public IndexController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping({"/", "/index"})
    public String index(@RequestParam(value = "filter", required = false) String filter, Model model, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        List<Task> tasks;

        // Лог: проверяем, что пользователь есть
        if (currentUser == null) {
            log.warn("Пользователь не найден в сессии");
            return "redirect:/login";
        }

        log.info("Пользователь {} с часовой зоной {} запрашивает задачи", currentUser.getLogin(), currentUser.getTimezone());

        switch (filter != null ? filter : "all") {
            case "completed":
                tasks = taskService.findCompleted();
                log.info("Загружены завершенные задачи: {}", tasks.size());
                break;
            case "active":
                tasks = taskService.findActive();
                log.info("Загружены активные задачи: {}", tasks.size());
                break;
            default:
                tasks = taskService.findAll();
                filter = "all";
                log.info("Загружены все задачи: {}", tasks.size());
        }
//        List<TaskDto> taskDtos = tasks.stream()
//                .map(task -> new TaskDto(task, currentUser.getTimezone()))
//                .collect(Collectors.toList());
        List<TaskDto> taskDtos = tasks.stream()
                .map(task -> {
                    log.debug("Конвертируем задачу {} с датой {}", task.getId(), task.getCreated());
                    return new TaskDto(task, currentUser.getTimezone());
                })
                .collect(Collectors.toList());

        model.addAttribute("tasks", taskDtos);
        model.addAttribute("filter", filter);

        log.info("Контроллер отдал в шаблон {} задач", taskDtos.size());
        return "index";
    }
}
