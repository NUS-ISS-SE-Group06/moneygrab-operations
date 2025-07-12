package com.moola.fx.moneychanger.operations.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "locations")
public class Locations {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String locationName;

    @Column(nullable = false, length = 10)
    private String countryCode;

    private String createdAt;

    private Integer createdBy;

    private Boolean isDeleted;

}
