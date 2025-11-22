// dto/admin/RecentOrderDto.java
package com.electromart.backend.dto.admin;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class RecentOrderDto {
    private Long id;
    private String code;      // mã đơn
    private LocalDateTime createdAt;
    private String status;    // PENDING / PAID / CANCELED...
    private String customerName;
    private long totalAmount;
}
