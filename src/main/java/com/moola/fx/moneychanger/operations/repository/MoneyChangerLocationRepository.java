package com.moola.fx.moneychanger.operations.repository;

import com.moola.fx.moneychanger.operations.model.MoneyChanger;
import com.moola.fx.moneychanger.operations.model.MoneyChangerLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface MoneyChangerLocationRepository extends JpaRepository<MoneyChangerLocation, Long> {

    List<MoneyChangerLocation> findByMoneyChangerAndIsDeletedFalse(MoneyChanger moneyChanger);
    //Add this method to fetch only non-deleted (active) locations
    @Transactional
    @Modifying
    @Query("UPDATE MoneyChangerLocation l SET l.isDeleted = true WHERE l.moneyChanger.id = :moneyChangerId")
    void softDeleteAllByMoneyChangerId(@Param("moneyChangerId") Long moneyChangerId);
}

