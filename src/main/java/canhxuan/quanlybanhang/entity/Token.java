package canhxuan.quanlybanhang.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 512, unique = true)
    private String token;

    private boolean revoked;
    private boolean expired;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Token(String jwtToken, boolean b, boolean b1, User user) {
        this.token = jwtToken;
        this.revoked = false;
        this.expired = false;
        this.user = user;
    }
    public Token() {};
}
