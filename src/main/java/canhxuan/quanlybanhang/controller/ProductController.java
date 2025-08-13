package canhxuan.quanlybanhang.controller;

import canhxuan.quanlybanhang.dto.ApiResponse;
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
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ApiResponse<Product> getById(@PathVariable int id) {
        ApiResponse<Product> response = new ApiResponse<>();
        response.setCode(200);
        response.setMessage("Success");
        response.setResult(productService.getById(id));
        return response;
    }

    @PostMapping("/create")
    public ResponseEntity<String> create(@RequestBody Product product) {
        productService.create(product);
        return ResponseEntity.status(HttpStatus.CREATED).body("Create product successfully");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> update(@PathVariable int id, @RequestBody Product product) {
        productService.update(id, product);
        return ResponseEntity.status(HttpStatus.OK).body("Update product successfully");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable int id) {
        productService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body("Delete product successfully");
    }
}
