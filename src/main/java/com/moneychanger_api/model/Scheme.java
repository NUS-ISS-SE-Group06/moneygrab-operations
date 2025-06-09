package com.moneychanger_api.model;

import lombok.Data;
import jakarta.persistence.*;

@Data
@Entity
@Table(name = "scheme")
public class Scheme {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="name",nullable = false, unique = true, length = 100)
    private String nameTag;

    @Column(length = 500)
    private String description;

    @Column(name = "is_default", nullable = false)
    private Boolean isDefault = false;
}
