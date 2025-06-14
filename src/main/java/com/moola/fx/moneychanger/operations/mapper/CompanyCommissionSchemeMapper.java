package com.moola.fx.moneychanger.operations.mapper;


import com.moola.fx.moneychanger.operations.dto.CompanyCommissionSchemeDTO;
import com.moola.fx.moneychanger.operations.model.CompanyCommissionScheme;
import com.moola.fx.moneychanger.operations.model.MoneyChanger;
import com.moola.fx.moneychanger.operations.model.Scheme;
import org.springframework.stereotype.Component;

@Component
public class CompanyCommissionSchemeMapper {

    public CompanyCommissionSchemeDTO toDTO(CompanyCommissionScheme entity) {
        if (entity == null) return null;

        CompanyCommissionSchemeDTO dto = new CompanyCommissionSchemeDTO();
        dto.setId(entity.getId());

        if (entity.getMoneyChangerId() != null) {
            dto.setMoneyChangerId(entity.getMoneyChangerId().getId().intValue());
            dto.setCompanyName(entity.getMoneyChangerId().getCompanyName());
        }

        if (entity.getSchemeId() != null) {
            dto.setSchemeId(entity.getSchemeId().getId());
            dto.setNameTag(entity.getSchemeId().getNameTag());
        }

        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        dto.setCreatedBy(entity.getCreatedBy());
        dto.setUpdatedBy(entity.getUpdatedBy());
        dto.setIsDeleted(entity.getIsDeleted());

        return dto;
    }

    public CompanyCommissionScheme toEntity(CompanyCommissionSchemeDTO dto) {
        if (dto == null) return null;

        CompanyCommissionScheme entity = new CompanyCommissionScheme();
        entity.setId(dto.getId());

        if (dto.getMoneyChangerId() != null) {
            MoneyChanger mc = new MoneyChanger();
            mc.setId(dto.getMoneyChangerId().longValue());
            mc.setCompanyName(dto.getCompanyName());

            entity.setMoneyChangerId(mc);
        }

        if (dto.getSchemeId() != null) {
            Scheme scheme = new Scheme();
            scheme.setId(dto.getSchemeId());
            scheme.setNameTag(dto.getNameTag());

            entity.setSchemeId(scheme);
        }

        entity.setCreatedAt(dto.getCreatedAt());
        entity.setUpdatedAt(dto.getUpdatedAt());
        entity.setCreatedBy(dto.getCreatedBy());
        entity.setUpdatedBy(dto.getUpdatedBy());
        entity.setIsDeleted(dto.getIsDeleted());

        return entity;
    }
}

