package com.moola.fx.moneychanger.operations.mapper;

import com.moola.fx.moneychanger.operations.dto.ComputeRateDTO;
import com.moola.fx.moneychanger.operations.model.ComputeRate;
import com.moola.fx.moneychanger.operations.model.ComputeRateId;
import com.moola.fx.moneychanger.operations.model.MoneyChanger;
import org.springframework.stereotype.Component;

@Component
public class ComputeRateMapper {

    public ComputeRateDTO toDTO(ComputeRate entity) {
        ComputeRateDTO dto = new ComputeRateDTO();
        dto.setCurrencyCode(entity.getId().getCurrencyCode());
        dto.setMoneyChangerId(entity.getId().getMoneyChangerId());

        dto.setUnit(entity.getUnit());
        dto.setRawBid(entity.getRawBid());
        dto.setRawAsk(entity.getRawAsk());
        dto.setSpread(entity.getSpread());
        dto.setSkew(entity.getSkew());
        dto.setWsBid(entity.getWsBid());
        dto.setWsAsk(entity.getWsAsk());

        dto.setRefBid(entity.getRefBid());
        dto.setDpBid(entity.getDpBid());
        dto.setMarBid(entity.getMarBid());
        dto.setCfBid(entity.getCfBid());
        dto.setRtBid(entity.getRtBid());

        dto.setRefAsk(entity.getRefAsk());
        dto.setDpAsk(entity.getDpAsk());
        dto.setMarAsk(entity.getMarAsk());
        dto.setCfAsk(entity.getCfAsk());
        dto.setRtAsk(entity.getRtAsk());

        dto.setProcessedAt(entity.getProcessedAt());
        dto.setCreatedBy(entity.getCreatedBy());
        dto.setUpdatedBy(entity.getUpdatedBy());

        return dto;
    }

    public ComputeRate toEntity(ComputeRateDTO dto) {
        ComputeRate entity = new ComputeRate();

        ComputeRateId id = new ComputeRateId();
        id.setCurrencyCode(dto.getCurrencyCode());
        id.setMoneyChangerId(dto.getMoneyChangerId());
        entity.setId(id);

        MoneyChanger moneyChanger =new MoneyChanger();
        moneyChanger.setId(dto.getMoneyChangerId());
        entity.setMoneyChanger(moneyChanger);

        entity.setUnit(dto.getUnit());
        entity.setRawBid(dto.getRawBid());
        entity.setRawAsk(dto.getRawAsk());
        entity.setSpread(dto.getSpread());
        entity.setSkew(dto.getSkew());
        entity.setWsBid(dto.getWsBid());
        entity.setWsAsk(dto.getWsAsk());

        entity.setRefBid(dto.getRefBid());
        entity.setDpBid(dto.getDpBid());
        entity.setMarBid(dto.getMarBid());
        entity.setCfBid(dto.getCfBid());
        entity.setRtBid(dto.getRtBid());

        entity.setRefAsk(dto.getRefAsk());
        entity.setDpAsk(dto.getDpAsk());
        entity.setMarAsk(dto.getMarAsk());
        entity.setCfAsk(dto.getCfAsk());
        entity.setRtAsk(dto.getRtAsk());

        entity.setProcessedAt(dto.getProcessedAt());
        entity.setCreatedBy(dto.getCreatedBy());
        entity.setUpdatedBy(dto.getUpdatedBy());

        return entity;
    }
}
