// controller/AdminDashboardController.java
package com.electromart.backend.controller;

import com.electromart.backend.dto.admin.AdminDashboardSummaryDto;
import com.electromart.backend.dto.admin.LowStockProductDto;
import com.electromart.backend.dto.admin.RecentOrderDto;
import com.electromart.backend.service.AdminDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/dashboard")
@RequiredArgsConstructor
@CrossOrigin
public class AdminDashboardController {

    private final AdminDashboardService dashboardService;

    @GetMapping("/summary")
    public ResponseEntity<AdminDashboardSummaryDto> summary() {
        return ResponseEntity.ok(dashboardService.getSummary());
    }

    @GetMapping("/low-stock")
    public ResponseEntity<List<LowStockProductDto>> lowStock() {
        return ResponseEntity.ok(dashboardService.getLowStockTop10());
    }

    @GetMapping("/recent-orders")
    public ResponseEntity<List<RecentOrderDto>> recentOrders() {
        return ResponseEntity.ok(dashboardService.getRecentOrdersTop10());
    }
}
