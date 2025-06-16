package com.moola.fx.moneychanger.operations.service.impl;

import com.moola.fx.moneychanger.operations.model.MoneyChangerPhoto;
import com.moola.fx.moneychanger.operations.repository.MoneyChangerPhotoRepository;
import com.moola.fx.moneychanger.operations.service.MoneyChangerPhotoService;
import org.apache.tika.Tika;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Optional;

@Service
public class MoneyChangerPhotoServiceImpl implements MoneyChangerPhotoService {

    private final MoneyChangerPhotoRepository photoRepository;

    public MoneyChangerPhotoServiceImpl(MoneyChangerPhotoRepository photoRepository) {
        this.photoRepository = photoRepository;
    }

    @Override
    public Optional<MoneyChangerPhoto> getByMoneyChangerId(Long moneyChangerId) {
        return photoRepository.findByMoneyChangerIdAndIsDeletedFalse(moneyChangerId);
    }

    @Override
    public void saveOrUpdate(Long moneyChangerId, String base64Image, String filename) {
        if (base64Image == null || base64Image.isEmpty()) {
            return;
        }

        photoRepository.findByMoneyChangerIdAndIsDeletedFalse(moneyChangerId).ifPresent(existing -> {
            existing.setIsDeleted(1);
            photoRepository.save(existing);
        });

        byte[] decoded = Base64.getDecoder().decode(base64Image);
        MoneyChangerPhoto newPhoto = new MoneyChangerPhoto();
        newPhoto.setMoneyChangerId(moneyChangerId);
        newPhoto.setPhotoData(decoded);
        newPhoto.setPhotoFilename(filename);
        newPhoto.setPhotoMimetype(detectMimeType(decoded));
        newPhoto.setIsDeleted(0);
        photoRepository.save(newPhoto);
    }

    private String detectMimeType(byte[] bytes) {
        try {
            return new Tika().detect(bytes);
        } catch (Exception e) {
            return "application/octet-stream";
        }
    }
}
