package com.electromart.backend.controller;

import com.electromart.backend.dto.admin.AdminOrderDto;
import com.electromart.backend.model.TrangThaiDonHang;
import com.electromart.backend.service.AdminOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/orders")
@RequiredArgsConstructor
@CrossOrigin
public class AdminOrderController {

    private final AdminOrderService adminOrderService;

    // GET /api/admin/orders?page=0&size=20
    @GetMapping
    public Page<AdminOrderDto> getOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        // sort theo trường ngayDatHang trong DonHang
        PageRequest pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.DESC, "ngayDatHang")
        );
        return adminOrderService.getOrders(pageable);
    }

    // PATCH /api/admin/orders/{id}/status?status=DANG_XU_LY
    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateStatus(
            @PathVariable Long id,
            @RequestParam("status") TrangThaiDonHang status
    ) {
        adminOrderService.updateStatus(id, status);
        return ResponseEntity.noContent().build();
    }
}
