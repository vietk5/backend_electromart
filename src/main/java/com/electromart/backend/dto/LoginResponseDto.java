package com.electromart.backend.dto;

import lombok.*;

@Getter @Setter
@AllArgsConstructor
public class LoginResponseDto<T> {
    private boolean success;
    private String message;
    private T data;
}
