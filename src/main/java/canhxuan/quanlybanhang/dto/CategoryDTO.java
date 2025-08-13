package canhxuan.quanlybanhang.dto;

import canhxuan.quanlybanhang.entity.Product;
import lombok.Data;

import java.util.List;

@Data
public class CategoryDTO {
    private Integer id;

    private String name;

    private String description;
    private List<Product> products;
}
