package com.electromart.backend.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CheckoutRequest {

    private Long userId;
    private String receiverName;
    private String receiverPhone;
    private String receiverAddress;
    private String paymentMethod;
    private String voucherCode;
    private List<Long> productIds;
}
