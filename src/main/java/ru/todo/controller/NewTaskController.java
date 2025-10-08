package ru.todo.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.todo.model.Category;
import ru.todo.model.Task;
import ru.todo.model.User;
import ru.todo.service.CategoryService;
import ru.todo.service.TaskService;
import ru.todo.service.UserService;

import java.util.List;
import java.util.Set;

@Controller
public class NewTaskController {

    private final TaskService taskService;
    private final UserService userService;
    private final CategoryService categoryService;

    public NewTaskController(TaskService taskService, UserService userService, CategoryService categoryService) {
        this.taskService = taskService;
        this.userService = userService;
        this.categoryService = categoryService;
    }

    @GetMapping("/tasks/new")
    public String showCreateForm(Model model) {
        model.addAttribute("task", new Task());
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        return "new-task";
    }

    @PostMapping("/tasks")
    public String createTask(@ModelAttribute Task task,
                             @RequestParam(required = false) List<Integer> categoryIds,
                             HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser != null) {
            task.setUser(currentUser);
        }
        if (categoryIds != null && !categoryIds.isEmpty()) {
            List<Category> selectedCategories = categoryService.findByIds(categoryIds);
            task.setCategories(Set.copyOf(selectedCategories));
        }
        taskService.create(task);
        return "redirect:/";
    }
}