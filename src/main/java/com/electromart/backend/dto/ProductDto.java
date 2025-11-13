// ProductDto.java
package com.electromart.backend.dto;

import java.math.BigDecimal;

public record ProductDto(
        Long id,
        String name,
        String brand,
        String category,
        BigDecimal price,
        String imageUrl
) {}
