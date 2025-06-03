package com.lovemp.common.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * JPA实体抽象基类，提供审计属性
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
public abstract class AbstractJpaEntity {

    /**
     * 乐观锁版本号
     */
    @Version
    @Column(name = "version")
    private Long version;

    /**
     * 创建时间
     */
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * the creator id
     */
    @CreatedBy
    @Column(name = "created_by", updatable = false)
    private String createdBy;

    /**
     * 最后修改时间
     */
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * 最后修改人
     */
    @LastModifiedBy
    @Column(name = "updated_by")
    private String updatedBy;

    /**
     * 是否已删除（逻辑删除标记）
     */
    @Column(name = "is_deleted", nullable = false)
    private Boolean deleted = false;

    /**
     * 删除时间
     */
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    /**
     * 删除人
     */
    @Column(name = "deleted_by")
    private String deletedBy;

    /**
     * 标记为已删除
     *
     * @param deletedBy 删除人
     */
    public void markAsDeleted(String deletedBy) {
        this.deleted = true;
        this.deletedAt = LocalDateTime.now();
        this.deletedBy = deletedBy;
    }

    /**
     * 检查是否已被删除
     *
     * @return 是否已被删除
     */
    public boolean isDeleted() {
        return deleted;
    }
} 