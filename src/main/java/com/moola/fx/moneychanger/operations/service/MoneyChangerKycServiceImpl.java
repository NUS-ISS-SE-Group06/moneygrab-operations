package com.moola.fx.moneychanger.operations.service;

import com.moola.fx.moneychanger.operations.model.MoneyChangerKyc;
import com.moola.fx.moneychanger.operations.repository.MoneyChangerKycRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.time.Instant;

@Service
public class MoneyChangerKycServiceImpl implements MoneyChangerKycService {

    @Autowired
    private MoneyChangerKycRepository moneyChangerKycRepository;

    @Override
    public MoneyChangerKyc getByMoneyChangerId(Long moneyChangerId) {
        return moneyChangerKycRepository
            .findFirstByMoneyChangerIdAndIsDeletedFalse(moneyChangerId)
            .orElse(null);
    }

    @Override
    public void saveOrUpdate(Long moneyChangerId, MultipartFile kycFile) {
        MoneyChangerKyc kyc = moneyChangerKycRepository
            .findFirstByMoneyChangerIdAndIsDeletedFalse(moneyChangerId)
            .orElse(new MoneyChangerKyc());

        kyc.setMoneyChangerId(moneyChangerId);
        try {
            kyc.setDocumentData(kycFile.getBytes());
        } catch (Exception e) {
            throw new RuntimeException("Failed to read KYC file data", e);
        }
        kyc.setDocumentFilename(kycFile.getOriginalFilename());
        kyc.setDocumentMimetype(kycFile.getContentType());

        Timestamp now = Timestamp.from(Instant.now());
        if (kyc.getId() == null) {
            kyc.setCreatedAt(now);
            kyc.setCreatedBy(1); // adjust as needed
        }
        kyc.setUpdatedAt(now);
        kyc.setUpdatedBy(1); // adjust as needed
        kyc.setIsDeleted(false);

        moneyChangerKycRepository.save(kyc);
    }
}
