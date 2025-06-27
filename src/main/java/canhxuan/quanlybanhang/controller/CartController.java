package canhxuan.quanlybanhang.controller;

import canhxuan.quanlybanhang.entity.Cart;
import canhxuan.quanlybanhang.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/quanlybanhang/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    @PostMapping("/create")
    public ResponseEntity addToCart(@RequestParam int userId,
                                    @RequestParam int productId,
                                    @RequestParam int quantity) {
        cartService.addToCart(userId, productId, quantity);
        return ResponseEntity.ok("Product is added to cart");
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Cart> getCartByUser(@PathVariable int userId) {
        Cart cart = cartService.getCartByUserId(userId);
        return ResponseEntity.ok(cart);
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateCart(@RequestParam int itemId,
                                             @RequestParam int quantity) {
        cartService.updateCartItemQuantity(itemId, quantity);
        return ResponseEntity.ok("Update item quantity successfully");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> removeCartItem(@RequestParam int itemId){
        cartService.removeItem(itemId);
        return ResponseEntity.ok("Remove item from cart successfully");
    }
}
