package canhxuan.quanlybanhang.controller;

import canhxuan.quanlybanhang.dto.CategoryDTO;
import canhxuan.quanlybanhang.entity.Category;
import canhxuan.quanlybanhang.mapper.CategoryMapper;
import canhxuan.quanlybanhang.service.CategoryService;
import org.casbin.jcasbin.main.Enforcer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/quanlybanhang/categories")
@CrossOrigin("/*")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private Enforcer enforcer;
    @Autowired
    private CategoryMapper categoryMapper;

    @GetMapping
    public ResponseEntity<?> getAll(Authentication authentication) {
        String username = authentication.getName();
        String path = "/categories";
        String method = "GET";
        if(enforcer.enforce(username, path, method)) {
            return ResponseEntity.ok(categoryService.getAll());
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id, Authentication authentication) {
        String username = authentication.getName();
        String path = "/categories/" + id;
        String method = "GET";
        if(enforcer.enforce(username, path, method)) {
            return ResponseEntity.ok(categoryService.getById(id));
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }
    }

    @PostMapping("/create")
    public String create(@RequestBody CategoryDTO dto) {
        Category category = categoryMapper.toCategory(dto);
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
