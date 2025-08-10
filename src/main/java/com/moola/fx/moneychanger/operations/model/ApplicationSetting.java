package com.moola.fx.moneychanger.operations.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(
    name = "application_setting",
    uniqueConstraints = @UniqueConstraint(name = "uq_application_setting", columnNames = {"category", "setting_key"})
)
public class ApplicationSetting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String category;

    @Column(name = "setting_key", length = 100, nullable = false)
    private String settingKey;

    @Column(name = "setting_value", length = 100, nullable = false)
    private String settingValue;

    // DB maintains timestamps via DEFAULT/ON UPDATE; keep them non-intrusive for JPA.
    @Column(name = "created_at", insertable = false, updatable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false,
            columnDefinition = "TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "updated_by")
    private Long updatedBy;
}
