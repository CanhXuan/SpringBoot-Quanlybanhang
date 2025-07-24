package canhxuan.quanlybanhang.service.impl;

import canhxuan.quanlybanhang.entity.Product;
import canhxuan.quanlybanhang.repository.ProductRepository;
import canhxuan.quanlybanhang.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAll() {
        return productRepository.findAll();
    }

    @Cacheable(value = "products", key = "#id")
    public Product getById(int id) {
        return productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
    }

    @CachePut(value = "products", key = "#result.id")
    public Product create(Product product) {

        return productRepository.save(product);
    }

    @CachePut(value = "products", key = "#result.id")
    public Product update(int id, Product product) {
        productRepository.findById(id).orElseThrow(()-> new RuntimeException("Product not found"));
        product.setId(id);
        return productRepository.save(product);
    }

    public void delete(int id) {
        productRepository.deleteById(id);
    }
}
