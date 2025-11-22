package com.electromart.backend.dto;

import lombok.*;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageResponse<T> {

    private List<T> content;      // danh sách item
    private int page;             // trang hiện tại
    private int size;             // kích thước trang
    private long totalElements;   // tổng số phần tử
    private int totalPages;       // tổng số trang
    private boolean last;         // có phải trang cuối không

    // ✅ Hàm dùng chung: from(...)
    public static <T> PageResponse<T> from(Page<T> page) {
        return PageResponse.<T>builder()
                .content(page.getContent())
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .build();
    }

    // ✅ Hàm alias: fromPage(...) – đúng với chỗ bạn đang gọi trong controller
    public static <T> PageResponse<T> fromPage(Page<T> page) {
        return from(page);
    }
}
