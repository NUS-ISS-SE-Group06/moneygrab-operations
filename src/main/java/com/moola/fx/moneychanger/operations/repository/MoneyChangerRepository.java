package com.moola.fx.moneychanger.operations.repository;

import com.moola.fx.moneychanger.operations.model.MoneyChanger;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MoneyChangerRepository extends JpaRepository<MoneyChanger, Long> {
    List<MoneyChanger> findByIsDeletedFalse();
}