package canhxuan.quanlybanhang.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "casbin_rule")
public class CasbinPolicy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String ptype;
    private String v0;
    private String v1;
    private String v2;
    private String v3;
    private String v4;
    private String v5;
}
