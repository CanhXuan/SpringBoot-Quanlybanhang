package canhxuan.quanlybanhang.service;

import canhxuan.quanlybanhang.entity.Category;
import canhxuan.quanlybanhang.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CategoryService {
    public List<Category> getAll();

    public Category getById(int id);

    public void create(Category category);

    public void update(int id, Category category);

    public void delete(int id);
}