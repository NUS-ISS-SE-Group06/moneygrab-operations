package com.moola.fx.moneychanger.operations.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class MoneyChangerDTO {

    private Long id;
    private String companyName;
    private String email;
    private String address;
    private String country;
    private String postalCode;
    private String notes;
    private LocalDate dateOfIncorporation;
    private String uen;
    private Integer schemeId;
    private Integer isDeleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long createdBy;
    private Long updatedBy;
    private String kycBase64;
    private String kycFilename;
    private String logoBase64;
    private String logoFilename;
    private String photoMimetype;
    private String documentMimetype;
    private List<String> locations;
}
