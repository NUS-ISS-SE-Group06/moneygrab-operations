package com.moneychanger_api.repository;

import com.moneychanger_api.model.CommissionRate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommissionRateRepository extends JpaRepository<CommissionRate, Integer> {
    boolean existsByDescriptionIgnoreCase(String description);
}