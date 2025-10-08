package ru.todo.repository;

import ru.todo.model.Category;

import java.util.List;

public interface CategoryRepository {

    List<Category> getAllCategories();

    List<Category> findByIdIn(List<Integer> ids);
}
