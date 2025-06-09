package com.moneychanger_api.mapper;

import com.moneychanger_api.dto.CommissionRateDTO;
import com.moneychanger_api.model.CommissionRate;

public class CommissionRateMapper {

    public static CommissionRateDTO toDTO(CommissionRate entity) {
        CommissionRateDTO dto = new CommissionRateDTO();
        dto.setId(entity.getId());
        dto.setCurrencyId(entity.getCurrencyId().getId());
        dto.setCurrency(entity.getCurrencyId().getCurrency());
        dto.setSchemeId(entity.getSchemeId().getId());
        dto.setNameTag(entity.getSchemeId().getNameTag());
        dto.setRate(entity.getRate());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        dto.setCreatedBy(entity.getCreatedBy());
        dto.setUpdatedBy(entity.getUpdatedBy());
        dto.setIsDeleted(entity.getIsDeleted());
        return dto;
    }

}
