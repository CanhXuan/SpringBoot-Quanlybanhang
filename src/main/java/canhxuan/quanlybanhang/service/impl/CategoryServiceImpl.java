package canhxuan.quanlybanhang.service.impl;

import canhxuan.quanlybanhang.entity.Category;
import canhxuan.quanlybanhang.repository.CategoryRepository;
import canhxuan.quanlybanhang.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> getAll() {
        return categoryRepository.findAll();
    }

    @Cacheable(value = "categories", key = "#id")
    public Category getById(int id) {
        System.out.println("Get category by id: " + id);
        return categoryRepository.findById(id).orElse(null);
    }

    @CachePut(value = "categories", key = "#result.id")
    public Category create(Category category) {
        return categoryRepository.save(category);
    }

    @CachePut(value = "categories", key = "#result.id")
    public Category update(int id, Category category) {
        categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Category not found"));
        category.setId(id);
        return categoryRepository.save(category);
    }

    @CacheEvict(value = "categories", key = "#id")
    public void delete(int id) {
        categoryRepository.deleteById(id);
    }
}
