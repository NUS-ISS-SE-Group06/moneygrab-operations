package com.moola.fx.moneychanger.operations.repository;

import com.moola.fx.moneychanger.operations.model.CurrencyCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CurrencyRepository extends JpaRepository<CurrencyCode, Integer> {
    Optional<CurrencyCode> findByCurrencyIgnoreCase(String currency);
    boolean existsByCurrencyIgnoreCase(String currency);
}