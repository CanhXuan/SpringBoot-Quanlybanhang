//package canhxuan.quanlybanhang.entity;
//
//import jakarta.persistence.*;
//import lombok.Data;
//
//import java.time.LocalDateTime;
//
//@Entity
//@Data
//@Table(name = "Email_Verification_Token")
//public class EmailVerificationToken {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private int id;
//
//    private String token;
//    private LocalDateTime expiryDate;
//
//    @OneToOne
//    private User user;
//
//    public EmailVerificationToken(String token, LocalDateTime localDateTime, User user) {
//        this.token = token;
//        this.expiryDate = localDateTime;
//        this.user = user;
//    }
//
//    public EmailVerificationToken() {}
//}
