package com.moola.fx.moneychanger.operations.service;

import com.moola.fx.moneychanger.operations.model.MoneyChangerPhoto;
import com.moola.fx.moneychanger.operations.repository.MoneyChangerPhotoRepository;
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
    public Optional<MoneyChangerPhoto> getByMoneyChangerId(Long id) {
        return photoRepository.findByMoneyChangerIdAndIsDeletedFalse(id);
    }

    @Override
    public void save(Long moneyChangerId, String base64Image, String filename) {
        if (base64Image == null || base64Image.isEmpty()) {
            return;
        }

        photoRepository.findByMoneyChangerIdAndIsDeletedFalse(moneyChangerId).ifPresent(existing -> {
            existing.setIsDeleted(1);
            photoRepository.save(existing);
        });

        MoneyChangerPhoto newPhoto = new MoneyChangerPhoto();
        newPhoto.setMoneyChangerId(moneyChangerId);
        String base64Data = base64Image.contains(",")
                ? base64Image.substring(base64Image.indexOf(',') + 1) // strip prefix
                : base64Image;

        if (base64Data.trim().isEmpty()) {
            return; // prevent saving empty content and deleting old KYC
        }

        photoRepository.findByMoneyChangerIdAndIsDeletedFalse(moneyChangerId).ifPresent(existing -> {
            existing.setIsDeleted(1);
            photoRepository.save(existing);
        });


        byte[] decoded = Base64.getDecoder().decode(base64Data);
        newPhoto.setPhotoData(decoded);               // GOOD data
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
