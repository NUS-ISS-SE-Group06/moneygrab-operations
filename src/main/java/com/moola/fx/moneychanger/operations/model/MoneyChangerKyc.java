package com.moola.fx.moneychanger.operations.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "money_changer_kyc")
public class MoneyChangerKyc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "money_changer_id", nullable = false)
    private Long moneyChangerId;

    @Lob
    @Column(name = "document_data", nullable = false, columnDefinition = "LONGBLOB")
    private byte[] documentData;

    @Column(name = "document_filename")
    private String documentFilename;

    @Column(name = "document_mimetype")
    private String documentMimetype;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "created_by")
    private Integer createdBy;

    @Column(name = "updated_by")
    private Integer updatedBy;

    @Column(name = "is_deleted", nullable = false)
    private Integer isDeleted;

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;

        if (this.isDeleted == null) {
            this.isDeleted = 0;
        }

    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
        if (this.isDeleted == null) {
            this.isDeleted = 0;
        }
    }
}
