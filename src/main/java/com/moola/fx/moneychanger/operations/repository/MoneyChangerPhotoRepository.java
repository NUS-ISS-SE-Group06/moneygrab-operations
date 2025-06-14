package com.moola.fx.moneychanger.operations.repository;

import com.moola.fx.moneychanger.operations.model.MoneyChangerPhoto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MoneyChangerPhotoRepository extends JpaRepository<MoneyChangerPhoto, Long> {

    Optional<MoneyChangerPhoto> findFirstByMoneyChangerIdAndIsDeletedFalse(Long moneyChangerId);

}
