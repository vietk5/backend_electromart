package com.electromart.backend.controller;

import com.electromart.backend.dto.CartAddRequest;
import com.electromart.backend.dto.CartItemDto;
import com.electromart.backend.dto.CheckoutRequest;
import com.electromart.backend.service.CartService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // nếu bạn đang gọi từ mobile/web khác domain
public class CartController {

    private final CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<Void> addToCart(@RequestBody CartAddRequest req) {
        cartService.addToCart(req);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<CartItemDto>> getCart(@PathVariable Long userId) {
        return ResponseEntity.ok(cartService.getCart(userId));
    }
    @PostMapping("/checkout")
    public ResponseEntity<Long> checkout(@RequestBody CheckoutRequest request) {
        Long orderId = cartService.checkout(request);
        return ResponseEntity.ok(orderId);
    }
    @DeleteMapping("/{userId}/item/{productId}")
    public ResponseEntity<Void> removeItem(
            @PathVariable int userId,
            @PathVariable Long productId) {
        cartService.removeItem(userId, productId);
        return ResponseEntity.noContent().build();
    }

}
