package com.example.financialledgersystem.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Getter
@MappedSuperclass // Does not have own table but put itself to inherited
@EntityListeners(AuditingEntityListener.class) // AuditingEntityListener listens for this and children
public abstract class BaseTimeEntity { // abstract -> no need to create this entity

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

}
