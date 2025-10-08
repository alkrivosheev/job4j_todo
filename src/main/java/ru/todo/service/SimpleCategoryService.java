package ru.todo.service;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.todo.model.Category;
import ru.todo.repository.CategoryRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class SimpleCategoryService implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.getAllCategories();
    }

    @Override
    public List<Category> findByIds(List<Integer> ids) {
        return categoryRepository.findByIdIn(ids);
    }
}
