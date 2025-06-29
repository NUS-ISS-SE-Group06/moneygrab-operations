package com.moola.fx.moneychanger.operations.model;

import jakarta.persistence.*;

@Entity
@Table(name = "money_changer_photo")
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

    // === Getters and Setters ===

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getPhotoData() {
        return photoData;
    }

    public void setPhotoData(byte[] photoData) {
        this.photoData = photoData;
    }

    public String getPhotoFilename() {
        return photoFilename;
    }

    public void setPhotoFilename(String photoFilename) {
        this.photoFilename = photoFilename;
    }

    public String getPhotoMimetype() {
        return photoMimetype;
    }

    public void setPhotoMimetype(String photoMimetype) {
        this.photoMimetype = photoMimetype;
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
