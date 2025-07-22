package com.moola.fx.moneychanger.operations.repository;

import com.moola.fx.moneychanger.operations.model.ViewRatesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ViewRatesRepository extends JpaRepository<ViewRatesEntity, Long> {

    List<ViewRatesEntity> findByMoneyChangerId(Long moneyChangerId);

}
