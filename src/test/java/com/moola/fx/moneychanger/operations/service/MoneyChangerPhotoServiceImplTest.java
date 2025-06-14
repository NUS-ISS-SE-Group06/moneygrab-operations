package com.moola.fx.moneychanger.operations.service;

import com.moola.fx.moneychanger.operations.model.MoneyChangerPhoto;
import com.moola.fx.moneychanger.operations.repository.MoneyChangerPhotoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MoneyChangerPhotoServiceImplTest {

    @InjectMocks
    private MoneyChangerPhotoServiceImpl photoService;

    @Mock
    private MoneyChangerPhotoRepository photoRepository;

    @Mock
    private MultipartFile mockFile;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetByMoneyChangerId() {
        MoneyChangerPhoto photo = new MoneyChangerPhoto();
        photo.setId(1L);
        photo.setMoneyChangerId(100L);
        photo.setPhotoFilename("photo.jpg");

        when(photoRepository.findFirstByMoneyChangerIdAndIsDeletedFalse(100L)).thenReturn(Optional.of(photo));

        MoneyChangerPhoto result = photoService.getByMoneyChangerId(100L);
        assertNotNull(result);
        assertEquals("photo.jpg", result.getPhotoFilename());

        verify(photoRepository, times(1)).findFirstByMoneyChangerIdAndIsDeletedFalse(100L);
    }

    @Test
    void testSaveOrUpdate_New() throws Exception {
        when(photoRepository.findFirstByMoneyChangerIdAndIsDeletedFalse(100L)).thenReturn(Optional.empty());
        when(mockFile.getBytes()).thenReturn("photo data".getBytes());
        when(mockFile.getOriginalFilename()).thenReturn("photo.jpg");
        when(mockFile.getContentType()).thenReturn("image/jpeg");

        photoService.saveOrUpdate(100L, mockFile);

        verify(photoRepository, times(1)).save(any(MoneyChangerPhoto.class));
    }

    @Test
    void testSaveOrUpdate_UpdateExisting() throws Exception {
        MoneyChangerPhoto existingPhoto = new MoneyChangerPhoto();
        existingPhoto.setId(1L);
        existingPhoto.setMoneyChangerId(100L);
        existingPhoto.setPhotoFilename("old_photo.jpg");

        when(photoRepository.findFirstByMoneyChangerIdAndIsDeletedFalse(100L)).thenReturn(Optional.of(existingPhoto));
        when(mockFile.getBytes()).thenReturn("new photo data".getBytes());
        when(mockFile.getOriginalFilename()).thenReturn("new_photo.jpg");
        when(mockFile.getContentType()).thenReturn("image/jpeg");

        photoService.saveOrUpdate(100L, mockFile);

        verify(photoRepository, times(1)).save(existingPhoto);
        assertEquals("new_photo.jpg", existingPhoto.getPhotoFilename());
    }
}
