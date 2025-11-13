// TokenForgetPassword.java
package com.electromart.backend.model;

import com.electromart.backend.model.base.AuditEntity;
import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Entity @Table(name = "token_forget_password")
public class TokenForgetPassword extends AuditEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nguoi_dung_id")
    private NguoiDung nguoiDung;

    @Column(nullable = false, unique = true, length = 200)
    private String token;

    private Instant hetHanLuc;
    private boolean daDung = false;
}
