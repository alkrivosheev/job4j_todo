package ru.todo.service;

import ru.todo.model.Category;
import java.util.List;

public interface CategoryService {

    List<Category> getAllCategories();

    List<Category> findByIds(List<Integer> ids);
}
