package com.electromart.backend.dto;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class RegisterRequestDto {
    private String hoTen;
    private String email;
    private String matKhau;
}
