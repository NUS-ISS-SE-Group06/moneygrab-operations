package com.moola.fx.moneychanger.operations.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "currency_code") 
public class CurrencyCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 10)
    private String currency;

    @Column(length = 255)
    private String description;
}
