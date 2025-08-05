package canhxuan.quanlybanhang.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "password_reset_token")
public class PasswordResetToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String token;
    private LocalDateTime expiration;

    @ManyToOne
    private User user;

    public PasswordResetToken(String token, LocalDateTime localDateTime, User user) {
        this.token = token;
        this.expiration = localDateTime;
        this.user = user;
    }

    public PasswordResetToken() {}
}
