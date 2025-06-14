package com.moola.fx.moneychanger.operations.service;

import com.moola.fx.moneychanger.operations.model.MoneyChangerPhoto;
import com.moola.fx.moneychanger.operations.repository.MoneyChangerPhotoRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.time.Instant;

@Service
public class MoneyChangerPhotoServiceImpl implements MoneyChangerPhotoService {

    private final MoneyChangerPhotoRepository moneyChangerPhotoRepository;

    public MoneyChangerPhotoServiceImpl(MoneyChangerPhotoRepository moneyChangerPhotoRepository) {
        this.moneyChangerPhotoRepository = moneyChangerPhotoRepository;
    }

    @Override
    public MoneyChangerPhoto getByMoneyChangerId(Long moneyChangerId) {
        return moneyChangerPhotoRepository
                .findFirstByMoneyChangerIdAndIsDeletedFalse(moneyChangerId)
                .orElse(null);
    }

    @Override
    public void saveOrUpdate(Long moneyChangerId, MultipartFile photoFile) {
        MoneyChangerPhoto photo = moneyChangerPhotoRepository
                .findFirstByMoneyChangerIdAndIsDeletedFalse(moneyChangerId)
                .orElse(new MoneyChangerPhoto());

        photo.setMoneyChangerId(moneyChangerId);

        try {
            photo.setPhotoData(photoFile.getBytes());
        } catch (Exception e) {
            // ✅ Use specific exception type — SonarQube compliant
            throw new IllegalStateException("Failed to read photo file data", e);
        }

        photo.setPhotoFilename(photoFile.getOriginalFilename());
        photo.setPhotoMimetype(photoFile.getContentType());

        Timestamp now = Timestamp.from(Instant.now());

        if (photo.getId() == null) {
            photo.setCreatedAt(now);
            photo.setCreatedBy(1); // adjust as needed
        }

        photo.setUpdatedAt(now);
        photo.setUpdatedBy(1); // adjust as needed
        photo.setIsDeleted(false);

        moneyChangerPhotoRepository.save(photo);
    }
}
