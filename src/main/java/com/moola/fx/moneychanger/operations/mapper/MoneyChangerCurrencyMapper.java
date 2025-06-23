package com.moola.fx.moneychanger.operations.mapper;

import com.moola.fx.moneychanger.operations.dto.MoneyChangerCurrencyDTO;
import com.moola.fx.moneychanger.operations.model.CurrencyCode;
import com.moola.fx.moneychanger.operations.model.MoneyChanger;
import com.moola.fx.moneychanger.operations.model.MoneyChangerCurrency;
import org.springframework.stereotype.Component;

@Component
public class MoneyChangerCurrencyMapper {

    public MoneyChangerCurrencyDTO toDTO(MoneyChangerCurrency entity) {
        if (entity == null) return null;

        MoneyChangerCurrencyDTO dto = new MoneyChangerCurrencyDTO();
        dto.setId(entity.getId());
        if (entity.getMoneyChangerId() != null) {
            dto.setMoneyChangerId(entity.getMoneyChangerId().getId().intValue());
            dto.setCompanyName(entity.getMoneyChangerId().getCompanyName());
        }
        if (entity.getCurrencyId() != null) {
            dto.setCurrencyId(entity.getCurrencyId().getId());
            dto.setCurrencyCode(entity.getCurrencyId().getCurrency());
            dto.setCurrencyDescription(entity.getCurrencyId().getDescription());
        }
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        dto.setCreatedBy(entity.getCreatedBy());
        dto.setUpdatedBy(entity.getUpdatedBy());
        dto.setIsDeleted(entity.getIsDeleted());
        return dto;
    }

    public MoneyChangerCurrency toEntity(MoneyChangerCurrencyDTO dto) {
        if (dto == null) return null;

        MoneyChangerCurrency entity = new MoneyChangerCurrency();
        entity.setId(dto.getId());

        if (dto.getMoneyChangerId() != null) {
            MoneyChanger moneyChanger = new MoneyChanger();
            moneyChanger.setId(dto.getMoneyChangerId().longValue());
            entity.setMoneyChangerId(moneyChanger);
        }
        if (dto.getCurrencyId() != null) {
            CurrencyCode currency = new CurrencyCode();
            currency.setId(dto.getCurrencyId());
            entity.setCurrencyId(currency);
        }
        entity.setCreatedAt(dto.getCreatedAt());
        entity.setUpdatedAt(dto.getUpdatedAt());
        entity.setCreatedBy(dto.getCreatedBy());
        entity.setUpdatedBy(dto.getUpdatedBy());
        entity.setIsDeleted(dto.getIsDeleted());

        return entity;
    }
}
