/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.electromart.backend.controller;

import com.electromart.backend.dto.OrderDetailDto;
import com.electromart.backend.service.UserOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}

