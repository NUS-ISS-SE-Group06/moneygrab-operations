package com.moola.fx.moneychanger.operations.repository;

import com.moola.fx.moneychanger.operations.model.ApplicationSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationSettingRepository extends JpaRepository<ApplicationSetting, Long> {

    List<ApplicationSetting> findByCategoryIgnoreCase(String category);

    Optional<ApplicationSetting> findByCategoryIgnoreCaseAndSettingKeyIgnoreCase(String category, String settingKey);

    boolean existsByCategoryIgnoreCaseAndSettingKeyIgnoreCase(String category, String settingKey);
}
