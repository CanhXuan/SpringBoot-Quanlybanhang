package canhxuan.quanlybanhang.service.impl;

import canhxuan.quanlybanhang.entity.Product;
import canhxuan.quanlybanhang.repository.ProductRepository;
import canhxuan.quanlybanhang.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAll() {
        return productRepository.findAll();
    }

    public Product getById(int id) {
        return productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public void create(Product product) {
        productRepository.save(product);
    }

    public void update(int id, Product product) {
        productRepository.findById(id).orElseThrow(()-> new RuntimeException("Product not found"));
        product.setId(id);
        productRepository.save(product);
    }

    public void delete(int id) {
        productRepository.deleteById(id);
    }
}
