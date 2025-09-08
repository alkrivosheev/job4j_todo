package ru.todo.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "login")
})
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 30)
    private String login;

    @Column(nullable = false, length = 30)
    private String password;
}
