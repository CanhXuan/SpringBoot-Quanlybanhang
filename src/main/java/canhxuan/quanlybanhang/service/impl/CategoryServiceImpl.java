package canhxuan.quanlybanhang.service.impl;

import canhxuan.quanlybanhang.entity.Category;
import canhxuan.quanlybanhang.repository.CategoryRepository;
import canhxuan.quanlybanhang.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> getAll() {
        return categoryRepository.findAll();
    }

    public Category getById(int id) {
        return categoryRepository.findById(id).orElse(null);
    }

    public void create(Category category) {
        categoryRepository.save(category);
    }

    public void update(int id, Category category) {
        categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Category not found"));
        category.setId(id);
        categoryRepository.save(category);
    }

    public void delete(int id) {
        categoryRepository.deleteById(id);
    }
}
