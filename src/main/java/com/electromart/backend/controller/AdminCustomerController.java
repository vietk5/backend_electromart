package com.electromart.backend.controller;

import com.electromart.backend.dto.PageResponse;
import com.electromart.backend.dto.admin.AdminCustomerDto;
import com.electromart.backend.service.AdminCustomerService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/customers")
public class AdminCustomerController {

    private final AdminCustomerService service;

    public AdminCustomerController(AdminCustomerService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<PageResponse<AdminCustomerDto>> list(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        boolean newestFirst = !"asc".equalsIgnoreCase(sortDir);
        Page<AdminCustomerDto> p = service.searchCustomers(search, page, size, newestFirst);
        return ResponseEntity.ok(PageResponse.fromPage(p));
    }

    @PostMapping("/{id}/toggle-block")
    public ResponseEntity<Void> toggleBlock(@PathVariable Long id) {
        service.toggleBlock(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }
}
