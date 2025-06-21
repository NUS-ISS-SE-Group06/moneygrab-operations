package com.moola.fx.moneychanger.operations.repository;

import com.moola.fx.moneychanger.operations.model.MoneyChangerKyc;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface MoneyChangerKycRepository extends JpaRepository<MoneyChangerKyc, Long> {
    Optional<MoneyChangerKyc> findByMoneyChangerIdAndIsDeletedFalse(Long moneyChangerId);
}
