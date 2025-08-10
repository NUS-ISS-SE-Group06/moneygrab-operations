package com.moola.fx.moneychanger.operations.service;

import com.moola.fx.moneychanger.operations.dto.ApplicationSettingDTO;
import com.moola.fx.moneychanger.operations.exception.DuplicateResourceException;
import com.moola.fx.moneychanger.operations.exception.ResourceNotFoundException;
import com.moola.fx.moneychanger.operations.model.ApplicationSetting;
import com.moola.fx.moneychanger.operations.repository.ApplicationSettingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplicationSettingServiceImpl implements ApplicationSettingService {

    private static final String ERR_NOT_FOUND_PREFIX = "Application setting not found: ";
    private static final String ERR_DUPLICATE_FMT =
            "Duplicate application setting for category=%s, key=%s";

    private final ApplicationSettingRepository repository;

    public ApplicationSettingServiceImpl(ApplicationSettingRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<ApplicationSettingDTO> listAll() {
        return repository.findAll().stream().map(this::toDTO).toList();
    }

    @Override
    public ApplicationSettingDTO get(Long id) {
        ApplicationSetting entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ERR_NOT_FOUND_PREFIX + id));
        return toDTO(entity);
    }

    @Override
    public List<ApplicationSettingDTO> listByCategory(String category) {
        return repository.findByCategoryIgnoreCase(category).stream().map(this::toDTO).toList();
    }

    @Override
    public ApplicationSettingDTO getByCategoryAndKey(String category, String settingKey) {
        ApplicationSetting entity = repository
                .findByCategoryIgnoreCaseAndSettingKeyIgnoreCase(category, settingKey)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Application setting not found for category=" + category + ", key=" + settingKey));
        return toDTO(entity);
    }

    @Override
    public ApplicationSettingDTO create(ApplicationSettingDTO dto) {
        // protect unique (category, setting_key)
        if (repository.existsByCategoryIgnoreCaseAndSettingKeyIgnoreCase(dto.getCategory(), dto.getSettingKey())) {
            throw new DuplicateResourceException(String.format(
                    ERR_DUPLICATE_FMT, dto.getCategory(), dto.getSettingKey()));
        }
        ApplicationSetting toSave = toEntity(dto);
        ApplicationSetting saved = repository.save(toSave);
        return toDTO(saved);
    }

    @Override
    public ApplicationSettingDTO update(Long id, ApplicationSettingDTO dto) {
        ApplicationSetting existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ERR_NOT_FOUND_PREFIX + id));

        // If either category or key changes, ensure uniqueness
        boolean changingIdentity =
                !existing.getCategory().equalsIgnoreCase(dto.getCategory())
                        || !existing.getSettingKey().equalsIgnoreCase(dto.getSettingKey());

        if (changingIdentity &&
                repository.existsByCategoryIgnoreCaseAndSettingKeyIgnoreCase(dto.getCategory(), dto.getSettingKey())) {
            throw new DuplicateResourceException(String.format(
                    ERR_DUPLICATE_FMT, dto.getCategory(), dto.getSettingKey()));
        }

        existing.setCategory(dto.getCategory());
        existing.setSettingKey(dto.getSettingKey());
        existing.setSettingValue(dto.getSettingValue());
        existing.setCreatedBy(dto.getCreatedBy()); // keep if you want to allow writing createdBy
        existing.setUpdatedBy(dto.getUpdatedBy());

        ApplicationSetting saved = repository.save(existing);
        return toDTO(saved);
    }

    @Override
    public void delete(Long id) {
        ApplicationSetting existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ERR_NOT_FOUND_PREFIX + id));
        repository.delete(existing);
    }

    // ---- mapping helpers (kept private & simple; Sonar-friendly) ----
    private ApplicationSettingDTO toDTO(ApplicationSetting e) {
        ApplicationSettingDTO dto = new ApplicationSettingDTO();
        dto.setId(e.getId());
        dto.setCategory(e.getCategory());
        dto.setSettingKey(e.getSettingKey());
        dto.setSettingValue(e.getSettingValue());
        dto.setCreatedBy(e.getCreatedBy());
        dto.setUpdatedBy(e.getUpdatedBy());
        dto.setCreatedAt(e.getCreatedAt());
        dto.setUpdatedAt(e.getUpdatedAt());
        return dto;
    }

    private ApplicationSetting toEntity(ApplicationSettingDTO dto) {
        ApplicationSetting e = new ApplicationSetting();
        e.setId(dto.getId());
        e.setCategory(dto.getCategory());
        e.setSettingKey(dto.getSettingKey());
        e.setSettingValue(dto.getSettingValue());
        e.setCreatedBy(dto.getCreatedBy());
        e.setUpdatedBy(dto.getUpdatedBy());
        return e;
    }
}
