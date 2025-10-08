package ru.todo.repository;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.todo.model.Category;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@AllArgsConstructor
public class HibernateCategoryRepository implements CategoryRepository {

    private final CrudRepository crudRepository;

    @Override
    public List<Category> getAllCategories() {
        return crudRepository.query("FROM Category c ORDER BY c.id", Category.class);
    }

    @Override
    public List<Category> findByIdIn(List<Integer> ids) {
        return crudRepository.query("FROM Category c WHERE c.id IN :ids ORDER BY c.id", Category.class,
                Map.of("ids", ids)
                );
    }
}