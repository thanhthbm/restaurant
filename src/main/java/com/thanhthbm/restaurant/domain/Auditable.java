package com.thanhthbm.restaurant.domain;

import com.thanhthbm.restaurant.util.SecurityUtil;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@MappedSuperclass
@Getter
@Setter
public abstract class Auditable {
    Instant createdAt;
    String createdBy;
    Instant updatedAt;
    String updatedBy;

    protected void handleBeforePersist() {}
    protected void handleBeforeUpdate() {}
    protected void handleAfterPersist() {}

    @PrePersist
    private void _auditPrePersist() {
        this.createdBy = SecurityUtil.getCurrentUserLogin().orElse("");
        this.createdAt = Instant.now();
        handleBeforePersist();      // gọi hook của subclass
    }

    @PreUpdate
    private void _auditPreUpdate() {
        this.updatedBy = SecurityUtil.getCurrentUserLogin().orElse("");
        this.updatedAt = Instant.now();
        handleBeforeUpdate();       // gọi hook của subclass
    }

    @PostPersist
    private void _auditPostPersist() {
        handleAfterPersist();       // gọi hook của subclass (vd: sinh SKU dựa vào id)
    }
}
