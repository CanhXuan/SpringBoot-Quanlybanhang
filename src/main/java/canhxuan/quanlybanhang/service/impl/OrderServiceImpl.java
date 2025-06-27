package canhxuan.quanlybanhang.service.impl;

import canhxuan.quanlybanhang.entity.Cart;
import canhxuan.quanlybanhang.entity.CartItem;
import canhxuan.quanlybanhang.entity.Order;
import canhxuan.quanlybanhang.entity.OrderItem;
import canhxuan.quanlybanhang.repository.OrderRepository;
import canhxuan.quanlybanhang.repository.ProductRepository;
import canhxuan.quanlybanhang.service.CartService;
import canhxuan.quanlybanhang.service.OrderService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CartService cartService;

    @Transactional
    public Order createOrderFromCart(int userId) {
        Cart cart = cartService.getCartByUserId(userId);
        if (cart == null || cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }
        Order order = new Order();
        order.setUser(cart.getUser());

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (CartItem cartItem : cart.getItems()) {
            OrderItem item = new OrderItem();
            item.setProduct(cartItem.getProduct());
            item.setQuantity(cartItem.getQuantity());
            item.setPrice(cartItem.getProduct().getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
            item.setOrder(order);
            total = total.add(item.getPrice());
            orderItems.add(item);
        }
        order.setItems(orderItems);
        order.setTotalAmount(total);
        orderRepository.save(order);

//        cartService.clearCart(userId);
        return order;
    }

    public List<Order> getOrderByUserId(int userId) {
        return orderRepository.findByUserId(userId);
    }

    public Order getOrderById(int id) {
        return orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
    }
}
