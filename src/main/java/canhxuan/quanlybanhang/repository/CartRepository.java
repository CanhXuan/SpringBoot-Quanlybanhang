package canhxuan.quanlybanhang.repository;

import canhxuan.quanlybanhang.entity.Cart;
import canhxuan.quanlybanhang.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {
    Optional<Cart> findByUser(User user);
}
