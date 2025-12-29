package com.electromart.backend.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class ApplyVoucherResponse {
    public boolean valid;
    public String message;
    public BigDecimal soTienGiam;
    public BigDecimal tongSauGiam;
    public VoucherDto voucher; // có thể null
}
