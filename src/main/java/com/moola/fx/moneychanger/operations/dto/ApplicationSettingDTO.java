package com.moola.fx.moneychanger.operations.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ApplicationSettingDTO {
    private Long id;
    private String category;
    private String settingKey;
    private String settingValue;
    private Long createdBy;
    private Long updatedBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
