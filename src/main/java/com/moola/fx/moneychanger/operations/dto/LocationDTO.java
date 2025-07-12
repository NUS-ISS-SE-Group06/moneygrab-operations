package com.moola.fx.moneychanger.operations.dto;

import lombok.Data;
import java.sql.Timestamp;

@Data
public class LocationDTO {

    private Integer id;

    private String locationName;

    private Integer countryCode;

    private Timestamp createdAt;

    private Integer createdBy;
    
    private Boolean isDeleted;

}
