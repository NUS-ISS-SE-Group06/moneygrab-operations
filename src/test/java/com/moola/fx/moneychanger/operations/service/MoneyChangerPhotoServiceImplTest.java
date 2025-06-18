package com.moola.fx.moneychanger.operations.service;

import com.moola.fx.moneychanger.operations.model.MoneyChangerPhoto;
import com.moola.fx.moneychanger.operations.repository.MoneyChangerPhotoRepository;
import com.moola.fx.moneychanger.operations.service.MoneyChangerPhotoServiceImpl;
import org.apache.tika.Tika;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MoneyChangerPhotoServiceImplTest {

    @Mock
    private MoneyChangerPhotoRepository photoRepository;

    @InjectMocks
    private MoneyChangerPhotoServiceImpl photoService;

    private static final Long TEST_ID = 1L;

    private static final String VALID_BASE64_IMAGE =
            "iVBORw0KGgoAAAANSUhEUgAAAAUA" +  // <-- short PNG header base64
                    "AAAFCAYAAACNbyblAAAAHElEQVQI12P4" +
                    "//8/w38GIAXDIBKE0DHxgljNBAAO9TXL0Y4OHwAAAABJRU5ErkJggg==";

    @BeforeEach
    void setUp() {
        // No need for openMocks if @ExtendWith(MockitoExtension.class) is used
    }

    @Test
    void testSavePhoto_WhenPreviousExists() {
        MoneyChangerPhoto existingPhoto = new MoneyChangerPhoto();
        existingPhoto.setIsDeleted(0);

        when(photoRepository.findByMoneyChangerIdAndIsDeletedFalse(TEST_ID))
                .thenReturn(Optional.of(existingPhoto));

        photoService.saveOrUpdate(TEST_ID, VALID_BASE64_IMAGE, "logo.png");

        // Once for old soft delete, once for new insert
        verify(photoRepository, times(2)).save(any(MoneyChangerPhoto.class));
    }

    @Test
    void testSavePhoto_WithEmptyBase64_ShouldReturn() {
        photoService.saveOrUpdate(TEST_ID, "", "logo.jpg");
        verify(photoRepository, never()).save(any());
    }
}
