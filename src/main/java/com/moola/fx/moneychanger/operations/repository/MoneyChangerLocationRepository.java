package com.moola.fx.moneychanger.operations.repository;

import com.moola.fx.moneychanger.operations.model.MoneyChangerLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MoneyChangerLocationRepository extends JpaRepository<MoneyChangerLocation, Long> {
    List<MoneyChangerLocation> findByMoneyChangerIdAndIsDeletedFalse(Long moneyChangerId);
}


