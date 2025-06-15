package com.moola.fx.moneychanger.operations.dto;

import java.util.List;

public class MoneyChangerResponseDTO {

    private Long id;
    private String companyName;
    private String email;
    private String address;
    private String postalCode;
    private String notes;
    private String dateOfIncorporation;
    private String country;
    private String uen;
    private Integer schemeId;

    private byte[] photoData;
    private String photoFilename;
    private String photoMimetype;

    private byte[] kycDocumentData;
    private String kycDocumentFilename;
    private String kycDocumentMimetype;

    // === [NEW FIELDS for location] ===
    private String location;              // For POST a single location
    private List<String> locations;       // For GET multiple locations

    // === Getters and Setters ===

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getDateOfIncorporation() {
        return dateOfIncorporation;
    }

    public void setDateOfIncorporation(String dateOfIncorporation) {
        this.dateOfIncorporation = dateOfIncorporation;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getUen() {
        return uen;
    }

    public void setUen(String uen) {
        this.uen = uen;
    }

    public Integer getSchemeId() {
        return schemeId;
    }

    public void setSchemeId(Integer schemeId) {
        this.schemeId = schemeId;
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

    public byte[] getKycDocumentData() {
        return kycDocumentData;
    }

    public void setKycDocumentData(byte[] kycDocumentData) {
        this.kycDocumentData = kycDocumentData;
    }

    public String getKycDocumentFilename() {
        return kycDocumentFilename;
    }

    public void setKycDocumentFilename(String kycDocumentFilename) {
        this.kycDocumentFilename = kycDocumentFilename;
    }

    public String getKycDocumentMimetype() {
        return kycDocumentMimetype;
    }

    public void setKycDocumentMimetype(String kycDocumentMimetype) {
        this.kycDocumentMimetype = kycDocumentMimetype;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<String> getLocations() {
        return locations;
    }

    public void setLocations(List<String> locations) {
        this.locations = locations;
    }
}
