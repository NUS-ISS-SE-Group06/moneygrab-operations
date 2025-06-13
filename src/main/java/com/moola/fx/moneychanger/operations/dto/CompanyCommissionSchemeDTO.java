package com.moola.fx.moneychanger.operations.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class CompanyCommissionSchemeDTO {
    private Integer id;
    private Integer moneyChangerId;
    private String companyName;
    private Integer schemeId;
    private String nameTag;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Integer createdBy;
    private Integer updatedBy;
    private Boolean isDeleted;
}

