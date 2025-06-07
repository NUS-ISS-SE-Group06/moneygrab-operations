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

    @Column(nullable = false, unique = true)
    private String name;
}
