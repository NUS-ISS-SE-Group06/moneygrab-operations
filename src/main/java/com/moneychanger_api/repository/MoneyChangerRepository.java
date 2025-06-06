package com.moneychanger_api.repository;

import com.moneychanger_api.model.MoneyChanger;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MoneyChangerRepository extends JpaRepository<MoneyChanger, Long> {
    List<MoneyChanger> findByIsDeletedFalse();
}