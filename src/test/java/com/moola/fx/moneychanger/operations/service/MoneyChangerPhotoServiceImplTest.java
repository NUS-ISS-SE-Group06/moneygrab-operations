package com.moola.fx.moneychanger.operations.service;

import com.moola.fx.moneychanger.operations.repository.MoneyChangerPhotoRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    void testSave_EmptyBase64() {
        photoService.save(1L, "", "logo.png");
        verify(photoRepository, never()).save(any());
    }

    @Test
    void testDetectMimeType_ShouldReturnCorrectMimeType_ForJpeg() throws Exception {
        byte[] jpegBytes = new byte[]{(byte) 0xFF, (byte) 0xD8, (byte) 0xFF};
        var method = MoneyChangerPhotoServiceImpl.class.getDeclaredMethod("detectMimeType", byte[].class);
        method.setAccessible(true);
        String mimeType = (String) method.invoke(photoService, new Object[]{jpegBytes}); // wrapped
        assertEquals("image/jpeg", mimeType);
    }

    @Test
    void testDetectMimeType_ShouldReturnFallbackMimeType_OnError() throws Exception {
        // Intentionally null to simulate failure
        byte[] corruptedInput = null; // Intentionally null to test fallback MIME detection
        var method = MoneyChangerPhotoServiceImpl.class.getDeclaredMethod("detectMimeType", byte[].class);
        method.setAccessible(true);
        String mimeType = (String) method.invoke(photoService, new Object[]{corruptedInput}); // wrapped
        assertEquals("application/octet-stream", mimeType);
    }
}
