package com.moola.fx.moneychanger.operations.service;

import com.moola.fx.moneychanger.operations.model.MoneyChangerPhoto;
import com.moola.fx.moneychanger.operations.repository.MoneyChangerPhotoRepository;
import org.apache.tika.Tika;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MoneyChangerPhotoServiceImplTest {

    @Mock
    private MoneyChangerPhotoRepository photoRepository;

    @Mock
    private Tika tika;

    @InjectMocks
    private MoneyChangerPhotoServiceImpl photoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetByMoneyChangerId_WhenFound() {
        Long id = 1L;
        MoneyChangerPhoto mockPhoto = new MoneyChangerPhoto();
        when(photoRepository.findByMoneyChangerIdAndIsDeletedFalse(id)).thenReturn(Optional.of(mockPhoto));

        Optional<MoneyChangerPhoto> result = photoService.getByMoneyChangerId(id);

        assertTrue(result.isPresent());
        assertEquals(mockPhoto, result.get());
        verify(photoRepository).findByMoneyChangerIdAndIsDeletedFalse(id);
    }

    @Test
    void testGetByMoneyChangerId_WhenNotFound() {
        Long id = 1L;
        when(photoRepository.findByMoneyChangerIdAndIsDeletedFalse(id)).thenReturn(Optional.empty());

        Optional<MoneyChangerPhoto> result = photoService.getByMoneyChangerId(id);

        assertFalse(result.isPresent());
        verify(photoRepository).findByMoneyChangerIdAndIsDeletedFalse(id);
    }

    @Test
    void testSaveOrUpdate_WithBase64Photo() {
        Long id = 1L;
        String base64 = "iVBORw0KGgoAAAANSUhEUg==";
        String filename = "logo.png";

        MoneyChangerPhoto existing = new MoneyChangerPhoto();
        when(photoRepository.findByMoneyChangerIdAndIsDeletedFalse(id)).thenReturn(Optional.of(existing));
        when(tika.detect(any(byte[].class))).thenReturn("image/png");

        photoService.saveOrUpdate(id, base64, filename);

        // Two saves expected: soft-delete old + insert new
        verify(photoRepository, times(2)).save(any(MoneyChangerPhoto.class));
    }

    @Test
    void testSaveOrUpdate_EmptyBase64() {
        photoService.saveOrUpdate(1L, "", "logo.png");
        verify(photoRepository, never()).save(any());
    }
}
