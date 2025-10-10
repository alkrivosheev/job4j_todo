package ru.todo.dto;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import ru.todo.model.Category;
import ru.todo.model.Priority;
import ru.todo.model.Task;
import ru.todo.model.User;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

@Slf4j
@Data
public class TaskDto {
    private Integer id;
    private String description;
    private Instant created;
    private String createdFormatted;
    private boolean done;
    private User user;
    private Priority priority;
    private Set<Category> categories;

    public TaskDto(Task task, String userTimezone) {
        this.id = task.getId();
        this.description = task.getDescription();
        this.created = task.getCreated();
        this.done = task.isDone();
        this.user = task.getUser();
        this.priority = task.getPriority();
        this.categories = task.getCategories();

        if (task.getCreated() != null) {
            // Логируем оригинальное время
            log.debug("Оригинальное время задачи {}: {}", task.getId(), task.getCreated());

            // Предполагаем, что время в БД хранится в UTC
            ZonedDateTime utcTime = task.getCreated().atZone(ZoneId.of("UTC"));
            ZonedDateTime userTime = utcTime.withZoneSameInstant(ZoneId.of(userTimezone));

            this.createdFormatted = userTime.format(DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy"));

            // Логируем результат
            log.debug("Конвертированное время задачи {}: {} -> {}", task.getId(), task.getCreated(), this.createdFormatted);
        } else {
            this.createdFormatted = "N/A";
        }
    }
}