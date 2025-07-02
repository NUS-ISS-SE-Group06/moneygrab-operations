package com.moola.fx.moneychanger.operations.service;

import com.moola.fx.moneychanger.operations.model.MoneyChangerKyc;
import com.moola.fx.moneychanger.operations.repository.MoneyChangerKycRepository;

import org.apache.tika.Tika;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Optional;

@Service
public class MoneyChangerKycServiceImpl implements MoneyChangerKycService {

    private final MoneyChangerKycRepository kycRepository;

    public MoneyChangerKycServiceImpl(MoneyChangerKycRepository kycRepository) {
        this.kycRepository = kycRepository;
    }

    @Override
    public Optional<MoneyChangerKyc> getByMoneyChangerId(Long id) {
        return kycRepository.findByMoneyChangerIdAndIsDeletedFalse(id);
    }

    @Override
    public void save(Long moneyChangerId, String base64Document, String filename) {
        if (base64Document == null || base64Document.isEmpty()) {
            return;
        }

        kycRepository.findByMoneyChangerIdAndIsDeletedFalse(moneyChangerId).ifPresent(existing -> {
            existing.setIsDeleted(1);
            kycRepository.save(existing);
        });


        MoneyChangerKyc newKyc = new MoneyChangerKyc();
        newKyc.setMoneyChangerId(moneyChangerId);

        String base64Data = base64Document.contains(",")
                ? base64Document.substring(base64Document.indexOf(',') + 1) // strip prefix
                : base64Document;

        if (base64Data.trim().isEmpty()) {
            return; // prevent saving empty content and deleting old KYC
        }

        kycRepository.findByMoneyChangerIdAndIsDeletedFalse(moneyChangerId).ifPresent(existing -> {
            existing.setIsDeleted(1);
            kycRepository.save(existing);
        });

        byte[] decoded = Base64.getDecoder().decode(base64Data);
        newKyc.setDocumentData(decoded);               // GOOD data
        newKyc.setDocumentFilename(filename);
        newKyc.setDocumentMimetype(detectMimeType(decoded));
        newKyc.setIsDeleted(0);
        kycRepository.save(newKyc);
    }

    private String detectMimeType(byte[] bytes) {
        try {
            return new Tika().detect(bytes);
        } catch (Exception e) {
            return "application/octet-stream";
        }
    }
}
