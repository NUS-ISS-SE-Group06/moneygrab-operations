package com.moneychanger_api.mapper;

import com.moneychanger_api.dto.CommissionRateDTO;
import com.moneychanger_api.model.CommissionRate;
import com.moneychanger_api.model.CurrencyCode;
import com.moneychanger_api.model.Scheme;
import org.springframework.stereotype.Component;

@Component
public class CommissionRateMapper {

    public CommissionRateDTO toDTO(CommissionRate entity) {
        if (entity == null) return null;

        CommissionRateDTO dto = new CommissionRateDTO();

        dto.setId(entity.getId());

        if (entity.getCurrencyId() != null) {
            dto.setCurrencyId(entity.getCurrencyId().getId());
            dto.setCurrency(entity.getCurrencyId().getCurrency());
        }

        if (entity.getSchemeId() != null) {
            dto.setSchemeId(entity.getSchemeId().getId());
            dto.setNameTag(entity.getSchemeId().getNameTag());
        }

        dto.setRate(entity.getRate());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        dto.setCreatedBy(entity.getCreatedBy());
        dto.setUpdatedBy(entity.getUpdatedBy());
        dto.setIsDeleted(entity.getIsDeleted());

        return dto;
    }


    public CommissionRate toEntity(CommissionRateDTO dto) {
        if (dto == null) return null;

        CommissionRate entity = new CommissionRate();

        entity.setId(dto.getId());

        if (dto.getCurrencyId() != null) {
            CurrencyCode currency = new CurrencyCode();
            currency.setId(dto.getCurrencyId());
            entity.setCurrencyId(currency);
        }

        if (dto.getSchemeId() != null) {
            Scheme scheme = new Scheme();
            scheme.setId(dto.getSchemeId());
            entity.setSchemeId(scheme);
        }

        entity.setRate(dto.getRate());
        entity.setCreatedAt(dto.getCreatedAt());
        entity.setUpdatedAt(dto.getUpdatedAt());
        entity.setCreatedBy(dto.getCreatedBy());
        entity.setUpdatedBy(dto.getUpdatedBy());
        entity.setIsDeleted(dto.getIsDeleted());

        return entity;
    }


}
