package com.electromart.backend.dto.admin;

import lombok.Data;

@Data
public class AdminDashboardSummaryDto {
    private long totalCustomers;
    private long totalProducts;
    private long totalStock;     // tổng tồn kho (sum so_luong)
    private long totalOrders;    // tổng đơn (tất cả trạng thái)
}
