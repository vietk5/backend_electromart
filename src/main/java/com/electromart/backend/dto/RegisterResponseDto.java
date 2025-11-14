package com.electromart.backend.dto;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class RegisterResponseDto<T> {
    private boolean success;
    private String message;
    private T data;
}
