package com.electromart.backend.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartAddRequest {
    private Long userId;
    private Long productId;
    private int quantity;
}
