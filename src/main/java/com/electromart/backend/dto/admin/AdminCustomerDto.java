package com.electromart.backend.dto.admin;

import java.time.Instant;

public class AdminCustomerDto {
    public Long id;
    public String fullName;
    public String email;
    public String phone;
    public Instant createdAt;
    public boolean blocked;

    public AdminCustomerDto(Long id, String fullName, String email,
                            String phone, Instant createdAt, boolean blocked) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.createdAt = createdAt;
        this.blocked = blocked;
    }
}
