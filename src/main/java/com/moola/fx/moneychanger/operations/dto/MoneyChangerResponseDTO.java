package com.moola.fx.moneychanger.operations.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class MoneyChangerResponseDTO {

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

    // Base64 + filenames
    private String kycBase64;
    private String kycFilename;
    private String logoBase64;
    private String logoFilename;

    // Other metadata (optional mimetypes, not required by frontend)
    private String photoMimetype;
    private String documentMimetype;

    // Locations list
    private List<String> locations;

    // === Getters and Setters ===

}
