package com.moola.fx.moneychanger.operations.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "money_changer_kyc")
@Data
public class MoneyChangerKyc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(name = "document_data", columnDefinition = "LONGBLOB")
    private byte[] documentData;

    @Column(name = "document_filename")
    private String documentFilename;

    @Column(name = "document_mimetype")
    private String documentMimetype;

    @Column(name = "is_deleted")
    private Integer isDeleted;

    @Column(name = "money_changer_id")
    private Long moneyChangerId;
}
