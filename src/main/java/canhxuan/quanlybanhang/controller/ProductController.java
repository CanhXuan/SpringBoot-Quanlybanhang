package canhxuan.quanlybanhang.controller;

import canhxuan.quanlybanhang.entity.Product;
import canhxuan.quanlybanhang.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/quanlybanhang/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<List<Product>> getAll() {
        List<Product> products = productService.getAll();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getById(@PathVariable int id) {
        return new ResponseEntity<>(productService.getById(id), HttpStatus.OK);
    }

    @PostMapping("/create")
    public String create(@RequestBody Product product) {
        productService.create(product);
        return "Create product successfully";
    }

    @PutMapping("/update/{id}")
    public String update(@PathVariable int id, @RequestBody Product product) {
        productService.update(id, product);
        return "Update product successfully";
    }

    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable int id) {
        productService.delete(id);
        return "Delete product successfully";
    }
}
