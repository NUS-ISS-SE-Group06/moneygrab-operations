package com.moneychanger_api.model;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Entity
@Table(name = "scheme")
public class Scheme {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="name_tag",nullable = false, unique = true, length = 100)
    private String nameTag;

    @Column(length = 500)
    private String description;

    @Column(name = "is_default", nullable = false)
    private Boolean isDefault = false;

    private Integer updatedBy;

    @Column(name = "is_deleted", nullable = false, columnDefinition ="TINYINT(1) DEFAULT 0" )
    private Boolean isDeleted=false;

}
