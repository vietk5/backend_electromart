package com.electromart.backend.dto;

import lombok.*;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor @Builder
public class SpinResultResponse {
    private boolean success;
    private String label;
    private String type;   // "VOUCHER" / "MESSAGE" / "GIFT"
    private String value;  // voucherCode hoặc message/quà
    private String message; // message hiển thị cho user
}
