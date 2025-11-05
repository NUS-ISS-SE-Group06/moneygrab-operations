package com.moola.fx.moneychanger.operations.service;

import com.moola.fx.moneychanger.operations.dto.ApplicationSettingDTO;
import com.moola.fx.moneychanger.operations.exception.DuplicateResourceException;
import com.moola.fx.moneychanger.operations.exception.ResourceNotFoundException;
import com.moola.fx.moneychanger.operations.model.ApplicationSetting;
import com.moola.fx.moneychanger.operations.repository.ApplicationSettingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApplicationSettingServiceImplTest {

    @Mock
    private ApplicationSettingRepository repository;

    @InjectMocks
    private ApplicationSettingServiceImpl service;

    private ApplicationSetting entity(long id, String cat, String key, String val) {
        ApplicationSetting e = new ApplicationSetting();
        e.setId(id);
        e.setCategory(cat);
        e.setSettingKey(key);
        e.setSettingValue(val);
        e.setCreatedBy(1L);
        return e;
    }

    private ApplicationSettingDTO dto(String cat, String key, String val) {
        ApplicationSettingDTO d = new ApplicationSettingDTO();
        d.setCategory(cat);
        d.setSettingKey(key);
        d.setSettingValue(val);
        d.setCreatedBy(1L);
        return d;
    }

    @BeforeEach
    void init() {
        // nothing needed; MockitoExtension handles initialization
    }

    @Test
    void listAll_ok() {
        when(repository.findAll()).thenReturn(List.of(
                entity(1, "General", "site_name", "MoneyGrab FX"),
                entity(2, "UI", "theme", "dark")
        ));

        List<ApplicationSettingDTO> list = service.listAll();
        assertEquals(2, list.size());
        assertEquals("UI", list.get(1).getCategory());
    }

    @Test
    void get_ok() {
        when(repository.findById(5L)).thenReturn(Optional.of(entity(5, "Email", "smtp_port", "587")));

        ApplicationSettingDTO result = service.get(5L);
        assertEquals("Email", result.getCategory());
        assertEquals("smtp_port", result.getSettingKey());
    }

    @Test
    void get_notFound_throws() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.get(99L));
    }

    @Test
    void listByCategory_ok() {
        when(repository.findByCategoryIgnoreCase("Security"))
                .thenReturn(List.of(
                        entity(7, "Security", "password_min_length", "8"),
                        entity(8, "Security", "max_login_attempts", "5")
                ));

        List<ApplicationSettingDTO> list = service.listByCategory("Security");
        assertEquals(2, list.size());
        assertEquals("password_min_length", list.get(0).getSettingKey());
    }

    @Test
    void getByCategoryAndKey_ok() {
        when(repository.findByCategoryIgnoreCaseAndSettingKeyIgnoreCase("General", "timezone"))
                .thenReturn(Optional.of(entity(3, "General", "timezone", "Asia/Singapore")));

        ApplicationSettingDTO dto = service.getByCategoryAndKey("General", "timezone");
        assertEquals("Asia/Singapore", dto.getSettingValue());
    }

    @Test
    void create_ok() {
        ApplicationSettingDTO request = dto("UI", "items_per_page", "20");
        when(repository.existsByCategoryIgnoreCaseAndSettingKeyIgnoreCase("UI", "items_per_page"))
                .thenReturn(false);
        when(repository.save(any(ApplicationSetting.class))).thenAnswer(inv -> {
            ApplicationSetting e = inv.getArgument(0, ApplicationSetting.class);
            e.setId(42L);
            return e;
        });

        ApplicationSettingDTO created = service.create(request);
        assertEquals(42L, created.getId());
        assertEquals("UI", created.getCategory());
    }
   
    @Test
    void create_duplicate_throws() {
        ApplicationSettingDTO request = dto("UI", "theme", "dark");
        when(repository.existsByCategoryIgnoreCaseAndSettingKeyIgnoreCase("UI", "theme"))
                .thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> service.create(request));
    }

    @Test
    void update_ok_whenIdentityUnchanged() {
        var existing = entity(9, "Security", "password_min_length", "8");
        when(repository.findById(9L)).thenReturn(Optional.of(existing));
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        var request = dto("Security", "password_min_length", "10");
        var updated = service.update(9L, request);

        assertEquals("10", updated.getSettingValue());
    }
    @Test
    void update_notFound_throws() {
        when(repository.findById(404L)).thenReturn(Optional.empty());
        ApplicationSettingDTO req = dto("X", "Y", "Z");  // build outside the lambda
        assertThrows(ResourceNotFoundException.class, () -> service.update(404L, req));
    }


    @Test
    void delete_ok() {
        when(repository.findById(1L)).thenReturn(Optional.of(entity(1, "UI", "theme", "dark")));

        service.delete(1L);

        verify(repository).delete(any(ApplicationSetting.class));
    }

    @Test
    void delete_notFound_throws() {
        when(repository.findById(404L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.delete(404L));
    }
}
