package canhxuan.quanlybanhang.controller;

import canhxuan.quanlybanhang.entity.Order;
import canhxuan.quanlybanhang.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/quanlybanhang/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping("/create")
    public ResponseEntity<Order> create(@RequestParam int userId) {
        Order order = orderService.createOrderFromCart(userId);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Order>> getOrderByUserId(@PathVariable int userId) {
        return ResponseEntity.ok(orderService.getOrderByUserId(userId));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderById(@PathVariable int orderId) {
        return ResponseEntity.ok(orderService.getOrderById(orderId));
    }
}
