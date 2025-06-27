package canhxuan.quanlybanhang.service.impl;

import canhxuan.quanlybanhang.entity.Cart;
import canhxuan.quanlybanhang.entity.CartItem;
import canhxuan.quanlybanhang.entity.Product;
import canhxuan.quanlybanhang.entity.User;
import canhxuan.quanlybanhang.repository.CartItemRepository;
import canhxuan.quanlybanhang.repository.CartRepository;
import canhxuan.quanlybanhang.repository.ProductRepository;
import canhxuan.quanlybanhang.repository.UserRepository;
import canhxuan.quanlybanhang.service.CartService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    @Transactional
    public void addToCart(int userId, int productId, int quantity) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));
        Cart cart = cartRepository.findByUser(user).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUser(user);
            return cartRepository.save(newCart);
        });
        Optional<CartItem> existingItem = cart.getItems() != null ? cart.getItems().stream()
                .filter(i -> i.getProduct().getId().equals(productId)).findFirst()
                : Optional.empty();

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
            cartItemRepository.save(item);
        } else {
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setQuantity(quantity);
            newItem.setPrice(product.getPrice());
            cartItemRepository.save(newItem);
        }
        cartRepository.save(cart);
    }

    @Override
    public Cart getCartByUserId(int userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return cartRepository.findByUser(user).orElseThrow(() -> new RuntimeException("Cart not found"));
    }

    @Override
    public void updateCartItemQuantity(int itemId, int quantity) {
        CartItem item = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));
        item.setQuantity(quantity);
        cartItemRepository.save(item);
    }

    @Override
    public void removeItem(int itemId) {
        CartItem item = cartItemRepository.findById(itemId).orElseThrow(() -> new RuntimeException("Item not found"));
        cartItemRepository.delete(item);
    }
}
