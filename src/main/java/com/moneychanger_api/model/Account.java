package com.moneychanger_api.model;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long moneyChangerId;

    @Column(nullable = false)
    private String role; // e.g., "admin" or "staff"

    @Column(nullable = false, unique = true)
    private String email;

    @Column(updatable = false, insertable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp createdAt;

    @Column(insertable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private Timestamp updatedAt;

    private Long createdBy;
    private Long updatedBy;

    @Column(columnDefinition = "TINYINT(1) DEFAULT 0")
    private Boolean isDeleted = false;

    // Getters and Setters (or use Lombok @Getter @Setter if preferred)
}
