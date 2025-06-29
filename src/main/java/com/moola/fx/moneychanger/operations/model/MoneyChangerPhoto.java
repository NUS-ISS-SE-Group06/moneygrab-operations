package com.moola.fx.moneychanger.operations.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "money_changer_photo")
@Data
public class MoneyChangerPhoto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(name = "photo_data", columnDefinition = "LONGBLOB")
    private byte[] photoData;

    @Column(name = "photo_filename")
    private String photoFilename;

    @Column(name = "photo_mimetype")
    private String photoMimetype;

    @Column(name = "is_deleted")
    private Integer isDeleted;

    @Column(name = "money_changer_id")
    private Long moneyChangerId;
}
