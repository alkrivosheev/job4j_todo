package ru.todo.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
@Data
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 1000)
    private String description;

    @Column(name = "created", nullable = false)
    private LocalDateTime created = LocalDateTime.now();

    @Column(name = "done", nullable = false)
    private boolean done = false;
}