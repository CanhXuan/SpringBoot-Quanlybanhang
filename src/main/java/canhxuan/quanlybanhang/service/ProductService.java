package canhxuan.quanlybanhang.service;

import canhxuan.quanlybanhang.entity.Product;

import java.util.List;

public interface ProductService {
    public List<Product> getAll();

    public Product getById(int id);

    public void create(Product product);

    public void update(int id, Product product);

    public void delete(int id);
}
