package com.electromart.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CheckoutItem {
    private long productId;
    private String name;
    private String imageUrl;
    private long unitPrice;
    private int quantity;
}
