package canhxuan.quanlybanhang.service;

import canhxuan.quanlybanhang.entity.Order;

import java.util.List;

public interface OrderService {
    public Order createOrderFromCart(int userId);

    public List<Order> getOrderByUserId(int userId);

    public Order getOrderById(int id);
}
