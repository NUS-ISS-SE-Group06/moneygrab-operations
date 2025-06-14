package com.moola.fx.moneychanger.operations.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Table(name = "money_changer_photo")
@Getter
@Setter
public class MoneyChangerPhoto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "money_changer_id", nullable = false)
    private Long moneyChangerId;

    @Lob
    @Column(name = "photo_data", nullable = false)
    private byte[] photoData;

    @Column(name = "photo_filename")
    private String photoFilename;

    @Column(name = "photo_mimetype")
    private String photoMimetype;

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
