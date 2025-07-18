package com.moola.fx.moneychanger.operations.repository;

import com.moola.fx.moneychanger.operations.dto.RateDTO;
import com.moola.fx.moneychanger.operations.model.ViewRatesEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ViewRatesRepository extends JpaRepository<ViewRatesEntity, Long> {

    @Query("""
        SELECT new com.moola.fx.moneychanger.operations.dto.RateDTO(
            v.currencyCode, v.unit, v.buyRate, v.sellRate, v.spread,
            v.refBid, v.refAsk, v.dpBid, v.dpAsk, v.marBid, v.marAsk,
            v.cbBid, v.cbAsk, v.cfBid, v.cfAsk, v.rtBid, v.rtAsk
        )
        FROM ViewRatesEntity v
        WHERE v.moneyChangerId = :moneyChangerId
    """)
    List<RateDTO> findRatesByMoneyChangerId(@Param("moneyChangerId") Long moneyChangerId);
}
