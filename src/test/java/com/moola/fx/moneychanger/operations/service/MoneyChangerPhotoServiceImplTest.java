package com.moola.fx.moneychanger.operations.service;

import com.moola.fx.moneychanger.operations.model.MoneyChangerPhoto;
import com.moola.fx.moneychanger.operations.repository.MoneyChangerPhotoRepository;
import org.apache.tika.Tika;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class MoneyChangerPhotoServiceImplTest {

    private MoneyChangerPhotoRepository photoRepository;
    private MoneyChangerPhotoServiceImpl photoService;

    @BeforeEach
    void setUp() {
        photoRepository = mock(MoneyChangerPhotoRepository.class);
        photoService = new MoneyChangerPhotoServiceImpl(photoRepository);
    }

    @Test
    void testSaveOrUpdate_EmptyBase64() {
        photoService.saveOrUpdate(1L, "", "logo.png");
        verify(photoRepository, never()).save(any());
    }

    @Test
    void testDetectMimeType_ShouldReturnCorrectMimeType_ForJpeg() throws Exception {
        // Simulate JPEG file signature: [0xFF, 0xD8, 0xFF]
        byte[] jpegBytes = new byte[]{(byte) 0xFF, (byte) 0xD8, (byte) 0xFF};

        // Use reflection to call private method without changing service visibility
        var method = MoneyChangerPhotoServiceImpl.class.getDeclaredMethod("detectMimeType", byte[].class);
        method.setAccessible(true);
        String mimeType = (String) method.invoke(photoService, (Object) jpegBytes);

        // This may return "image/jpeg" depending on Apache Tika version
        assertEquals("image/jpeg", mimeType);
    }

    @Test
    void testDetectMimeType_ShouldReturnFallbackMimeType_OnError() throws Exception {
        byte[] badData = null; // Null byte array triggers exception

        var method = MoneyChangerPhotoServiceImpl.class.getDeclaredMethod("detectMimeType", byte[].class);
        method.setAccessible(true);
        String mimeType = (String) method.invoke(photoService, (Object) badData);

        assertEquals("application/octet-stream", mimeType);
    }
}
