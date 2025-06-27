package canhxuan.quanlybanhang.service;

import canhxuan.quanlybanhang.entity.Cart;

public interface CartService {
    void addToCart(int userId, int productId, int quantity);
    Cart getCartByUserId (int userId);
    void updateCartItemQuantity(int itemId, int quantity);
    void removeItem(int itemId);
}
