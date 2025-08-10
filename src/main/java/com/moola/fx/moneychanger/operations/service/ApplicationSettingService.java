package com.moola.fx.moneychanger.operations.service;

import com.moola.fx.moneychanger.operations.dto.ApplicationSettingDTO;

import java.util.List;

public interface ApplicationSettingService {
    List<ApplicationSettingDTO> listAll();

    ApplicationSettingDTO get(Long id);

    List<ApplicationSettingDTO> listByCategory(String category);

    ApplicationSettingDTO getByCategoryAndKey(String category, String settingKey);

    ApplicationSettingDTO create(ApplicationSettingDTO dto);

    ApplicationSettingDTO update(Long id, ApplicationSettingDTO dto);

    void delete(Long id);
}
