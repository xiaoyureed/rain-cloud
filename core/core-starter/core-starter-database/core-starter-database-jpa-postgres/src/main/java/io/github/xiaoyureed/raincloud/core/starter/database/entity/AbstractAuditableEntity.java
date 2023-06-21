package io.github.xiaoyureed.raincloud.core.starter.database.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;

@MappedSuperclass
@Data
@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractAuditableEntity extends AbstractIdEntity {

    @CreatedBy
    protected String createdBy;

    @LastModifiedBy
    protected String modifiedBy;

    @Column(name = "created_at", updatable = false)
//    @CreationTimestamp
//    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    protected LocalDateTime createdAt;

    @LastModifiedDate
//    @UpdateTimestamp
//    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    protected LocalDateTime updatedAt;

}
