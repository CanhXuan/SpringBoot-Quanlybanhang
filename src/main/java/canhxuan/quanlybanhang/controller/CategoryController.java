package canhxuan.quanlybanhang.controller;

import canhxuan.quanlybanhang.entity.Category;
import canhxuan.quanlybanhang.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/quanlybanhang/categories")
@CrossOrigin("/*")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<Category>> getAll() {
        List<Category> categories = categoryService.getAll();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> getById(@PathVariable Integer id) {
        Category category = categoryService.getById(id);
        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    @PostMapping("/create")
    public String create(@RequestBody Category category) {
        categoryService.create(category);
        return "Create category successfully";
    }

    @PutMapping("/update/{id}")
    public String update(@PathVariable int id, @RequestBody Category category) {
        categoryService.update(id, category);
        return "update category successfully";
    }

    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable int id) {
        categoryService.delete(id);
        return "delete category successfully";
    }
}
