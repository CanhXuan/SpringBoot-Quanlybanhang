package canhxuan.quanlybanhang.repository;

import canhxuan.quanlybanhang.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface    CategoryRepository extends JpaRepository<Category, Integer> { }
