package com.moola.fx.moneychanger.operations.model;

import jakarta.persistence.*;

@Entity
@Table(name = "money_changer_kyc")
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

    // === Getters and Setters ===

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getDocumentData() {
        return documentData;
    }

    public void setDocumentData(byte[] documentData) {
        this.documentData = documentData;
    }

    public String getDocumentFilename() {
        return documentFilename;
    }

    public void setDocumentFilename(String documentFilename) {
        this.documentFilename = documentFilename;
    }

    public String getDocumentMimetype() {
        return documentMimetype;
    }

    public void setDocumentMimetype(String documentMimetype) {
        this.documentMimetype = documentMimetype;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Long getMoneyChangerId() {
        return moneyChangerId;
    }

    public void setMoneyChangerId(Long moneyChangerId) {
        this.moneyChangerId = moneyChangerId;
    }
}
