package com.electromart.backend.dto;

import lombok.*;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor @Builder
public class SpinStatusResponse {
    private boolean canSpin;
    private int remainingSpinsToday;
    private String message;
    private String nextSpinAt; // ISO string hoặc null
}
