package com.moola.fx.moneychanger.operations.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.moola.fx.moneychanger.operations.dto.ApplicationSettingDTO;
import com.moola.fx.moneychanger.operations.exception.DuplicateResourceException;
import com.moola.fx.moneychanger.operations.exception.GlobalExceptionHandler;
import com.moola.fx.moneychanger.operations.exception.ResourceNotFoundException;
import com.moola.fx.moneychanger.operations.service.ApplicationSettingService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ApplicationSettingControllerTest {

    @Mock
    private ApplicationSettingService service;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        ApplicationSettingController controller = new ApplicationSettingController(service);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    private static ApplicationSettingDTO dto(long id, String cat, String key, String val) {
        ApplicationSettingDTO d = new ApplicationSettingDTO();
        d.setId(id);
        d.setCategory(cat);
        d.setSettingKey(key);
        d.setSettingValue(val);
        d.setCreatedBy(1L);
        return d;
    }

    @Test
    void listAll_ok() throws Exception {
        when(service.listAll()).thenReturn(List.of(
                dto(1, "General", "site_name", "MoneyGrab FX"),
                dto(2, "Email", "smtp_port", "587")
        ));

        mockMvc.perform(get("/v1/application-settings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].category").value("General"))
                .andExpect(jsonPath("$[1].settingKey").value("smtp_port"));
    }

    @Test
    void listByCategory_ok() throws Exception {
        when(service.listByCategory("Email")).thenReturn(List.of(
                dto(3, "Email", "smtp_host", "smtp.mailtrap.io"),
                dto(4, "Email", "smtp_port", "587")
        ));

        mockMvc.perform(get("/v1/application-settings/category/Email"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].settingKey").value("smtp_host"));
    }

    @Test
    void getByCategoryAndKey_ok() throws Exception {
        when(service.getByCategoryAndKey("General", "timezone"))
                .thenReturn(dto(5, "General", "timezone", "Asia/Singapore"));

        mockMvc.perform(get("/v1/application-settings/category/General/key/timezone"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.settingValue").value("Asia/Singapore"));
    }

    @Test
    void getById_ok() throws Exception {
        when(service.get(7L)).thenReturn(dto(7, "UI", "theme", "dark"));

        mockMvc.perform(get("/v1/application-settings/7"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.settingKey").value("theme"));
    }

    @Test
    void getById_notFound_404() throws Exception {
        when(service.get(404L)).thenThrow(new ResourceNotFoundException("x"));

        mockMvc.perform(get("/v1/application-settings/404"))
                .andExpect(status().isNotFound());
    }

    @Test
    void create_ok() throws Exception {
        ApplicationSettingDTO request = dto(0, "UI", "theme", "dark");
        ApplicationSettingDTO created = dto(10, "UI", "theme", "dark");
        when(service.create(any(ApplicationSettingDTO.class))).thenReturn(created);

        mockMvc.perform(post("/v1/application-settings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/v1/application-settings/10"))
                .andExpect(jsonPath("$.id").value(10));
    }

    @Test
    void create_duplicate_409() throws Exception {
        ApplicationSettingDTO request = dto(0, "UI", "theme", "dark");
        when(service.create(any(ApplicationSettingDTO.class)))
                .thenThrow(new DuplicateResourceException("dup"));

        mockMvc.perform(post("/v1/application-settings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }

    @Test
    void update_ok() throws Exception {
        ApplicationSettingDTO request = dto(0, "Security", "password_min_length", "10");
        ApplicationSettingDTO updated = dto(9, "Security", "password_min_length", "10");
        when(service.update(9L, request)).thenReturn(updated);

        mockMvc.perform(put("/v1/application-settings/9")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(9));
    }

    @Test
    void delete_ok() throws Exception {
        doNothing().when(service).delete(12L);

        mockMvc.perform(delete("/v1/application-settings/12"))
                .andExpect(status().isNoContent());
    }
}
