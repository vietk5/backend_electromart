// src/main/java/com/electromart/backend/controller/AdminRevenueController.java
package com.electromart.backend.controller;

import com.electromart.backend.dto.admin.RevenuePointDto;
import com.electromart.backend.service.AdminRevenueService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/admin/revenue")
public class AdminRevenueController {

    private final AdminRevenueService revenueService;

    public AdminRevenueController(AdminRevenueService revenueService) {
        this.revenueService = revenueService;
    }

    @GetMapping
    public List<RevenuePointDto> getRevenue(
            @RequestParam String from,
            @RequestParam String to,
            @RequestParam(defaultValue = "DAY")
            AdminRevenueService.GroupBy groupBy   // ✅ dùng enum, không dùng String
    ) {
        LocalDate fromDate = LocalDate.parse(from);
        LocalDate toDate   = LocalDate.parse(to);

        return revenueService.getRevenue(fromDate, toDate, groupBy);
    }
}
