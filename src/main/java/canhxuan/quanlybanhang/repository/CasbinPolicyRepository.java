package canhxuan.quanlybanhang.repository;

import canhxuan.quanlybanhang.entity.CasbinPolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CasbinPolicyRepository extends JpaRepository<CasbinPolicy, Integer> {
}
