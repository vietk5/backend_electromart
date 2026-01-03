package com.electromart.backend.controller;

import com.electromart.backend.dto.OrderDetailDto;
import com.electromart.backend.dto.OrderDetailItemDto;
import com.electromart.backend.service.UserOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/orders")
@RequiredArgsConstructor
@CrossOrigin
public class UserOrderController {

    private final UserOrderService userOrderService;

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDetailDto> getOrderDetail(@PathVariable Long orderId) {
        return ResponseEntity.ok(userOrderService.getOrderDetail(orderId));
    }

    @GetMapping("/by-user/{userId}")
    public ResponseEntity<List<OrderDetailDto>> getOrdersByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(userOrderService.getOrdersByUser(userId));
    }
    
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelOrder(@PathVariable Long id) {
        userOrderService.cancelOrder(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/{id}/items")
    public ResponseEntity<List<OrderDetailItemDto>> getOrderItems(@PathVariable Long id) {
        return ResponseEntity.ok(userOrderService.getOrderItems(id));
    }
}
