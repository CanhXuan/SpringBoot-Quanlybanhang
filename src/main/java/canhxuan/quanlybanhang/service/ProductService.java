package canhxuan.quanlybanhang.service;

import canhxuan.quanlybanhang.entity.Product;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

public interface ProductService {
    public List<Product> getAll();

    public Product getById(int id);

    public Product create(Product product);

    public Product update(int id, Product product);

    public void delete(int id);
}
