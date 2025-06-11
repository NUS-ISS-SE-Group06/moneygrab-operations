package com.moola.fx.moneychanger.operations.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moola.fx.moneychanger.operations.exception.ResourceNotFoundException;
import com.moola.fx.moneychanger.operations.model.Scheme;
import com.moola.fx.moneychanger.operations.service.SchemeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class SchemeControllerTest {

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private SchemeService schemeService;

    @InjectMocks
    private SchemeController schemeController;

    /**
     * Advice to convert ResourceNotFoundException into JSON { "message": "…" } with 404 status
     */
    @RestControllerAdvice
    static class TestExceptionAdvice {
        @ExceptionHandler(ResourceNotFoundException.class)
        @ResponseStatus(HttpStatus.NOT_FOUND)
        public Map<String,String> handleNotFound(ResourceNotFoundException ex) {
            return Map.of("message", ex.getMessage());
        }
    }

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(schemeController)
                .setControllerAdvice(new TestExceptionAdvice())
                .build();
    }

    @Test
    void testListSchemes() throws Exception {
        Scheme scheme = new Scheme();
        scheme.setId(1);
        scheme.setNameTag("Basic");
        scheme.setDescription("Basic plan");
        scheme.setIsDefault(false);

        Mockito.when(schemeService.listAll()).thenReturn(List.of(scheme));

        mockMvc.perform(get("/v1/schemes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].nameTag", is("Basic")))
                .andExpect(jsonPath("$[0].description", is("Basic plan")))
                .andExpect(jsonPath("$[0].isDefault", is(false)));

        Mockito.verify(schemeService, times(1)).listAll();
    }

    @Test
    void testGetSchemeFound() throws Exception {
        Scheme scheme = new Scheme();
        scheme.setId(1);
        scheme.setNameTag("Standard");
        scheme.setDescription("Standard scheme");
        scheme.setIsDefault(true);

        Mockito.when(schemeService.get(1)).thenReturn(scheme);

        mockMvc.perform(get("/v1/schemes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nameTag", is("Standard")))
                .andExpect(jsonPath("$.description", is("Standard scheme")))
                .andExpect(jsonPath("$.isDefault", is(true)));

        Mockito.verify(schemeService, times(1)).get(1);
    }

    @Test
    void testGetSchemeNotFound() throws Exception {
        Mockito.when(schemeService.get(999))
                .thenThrow(new ResourceNotFoundException("Scheme with ID 999 not found"));

        mockMvc.perform(get("/v1/schemes/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value("Scheme with ID 999 not found"));

        Mockito.verify(schemeService, times(1)).get(999);
    }

    @Test
    void testCreateScheme() throws Exception {
        Scheme scheme = new Scheme();
        scheme.setId(1);
        scheme.setNameTag("Gold");
        scheme.setDescription("Gold plan");
        scheme.setIsDefault(true);

        Mockito.when(schemeService.save(any(Scheme.class))).thenReturn(scheme);

        mockMvc.perform(post("/v1/schemes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                      "nameTag": "Gold",
                      "description": "Gold plan",
                      "isDefault": true
                    }
                    """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nameTag", is("Gold")))
                .andExpect(jsonPath("$.description", is("Gold plan")))
                .andExpect(jsonPath("$.isDefault", is(true)));

        Mockito.verify(schemeService, times(1)).save(any(Scheme.class));
    }

    @Test
    void testUpdateScheme() throws Exception {
        Scheme existing = new Scheme();
        existing.setId(1);
        existing.setNameTag("Old Name");
        existing.setDescription("Old description");
        existing.setIsDefault(false);

        Scheme updated = new Scheme();
        updated.setId(1);
        updated.setNameTag("Updated");
        updated.setDescription("Updated description");
        updated.setIsDefault(false);

        Mockito.when(schemeService.get(1)).thenReturn(existing);
        Mockito.when(schemeService.save(any(Scheme.class))).thenReturn(updated);

        mockMvc.perform(put("/v1/schemes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                      "nameTag": "Updated",
                      "description": "Updated description",
                      "isDefault": false
                    }
                    """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nameTag", is("Updated")))
                .andExpect(jsonPath("$.description", is("Updated description")))
                .andExpect(jsonPath("$.isDefault", is(false)));

        Mockito.verify(schemeService, times(1)).get(1);
        Mockito.verify(schemeService, times(1)).save(any(Scheme.class));
    }

    @Test
    void testDeleteSchemeSuccess() throws Exception {
        Mockito.doNothing().when(schemeService).delete(1, 1);

        mockMvc.perform(delete("/v1/schemes/1")
                        .param("userId", "1"))
                .andExpect(status().isNoContent());

        Mockito.verify(schemeService, times(1)).delete(1, 1);
    }

    @Test
    void testDeleteSchemeNotFound() throws Exception {
        Mockito.doThrow(new ResourceNotFoundException("Scheme with ID 999 not found"))
                .when(schemeService).delete(999, 1);

        mockMvc.perform(delete("/v1/schemes/999")
                        .param("userId", "1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value("Scheme with ID 999 not found"));

        Mockito.verify(schemeService, times(1)).delete(999, 1);
    }
}
