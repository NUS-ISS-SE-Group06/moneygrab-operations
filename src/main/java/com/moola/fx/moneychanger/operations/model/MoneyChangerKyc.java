package com.moola.fx.moneychanger.operations.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Table(name = "money_changer_kyc")
@Getter
@Setter
public class MoneyChangerKyc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ✅ Corrected to Long
    @Column(name = "money_changer_id", nullable = false)
    private Long moneyChangerId;

    @Lob
    @Column(name = "document_data", nullable = false)
    private byte[] documentData;

    @Column(name = "document_filename")
    private String documentFilename;

    @Column(name = "document_mimetype")
    private String documentMimetype;

    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;

    @Column(name = "updated_at", nullable = false)
    private Timestamp updatedAt;

    @Column(name = "created_by")
    private Integer createdBy;

    @Column(name = "updated_by")
    private Integer updatedBy;

    @Column(name = "is_deleted")
    private Boolean isDeleted;
}
