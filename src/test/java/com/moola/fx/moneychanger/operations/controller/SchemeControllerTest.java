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
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class SchemeControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private MockMvc mockMvc;
    @Mock
    private SchemeService schemeService;

    @InjectMocks
    private SchemeController schemeController;

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

        when(schemeService.listAll()).thenReturn(List.of(scheme));

        mockMvc.perform(get("/v1/schemes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].nameTag", is("Basic")))
                .andExpect(jsonPath("$[0].description", is("Basic plan")))
                .andExpect(jsonPath("$[0].isDefault", is(false)));

        verify(schemeService, times(1)).listAll();
    }

    @Test
    void testCreateScheme() throws Exception {
        Scheme scheme = new Scheme();
        scheme.setId(1);
        scheme.setNameTag("Gold");
        scheme.setDescription("Gold plan");
        scheme.setIsDefault(true);

        when(schemeService.save(any(Scheme.class))).thenReturn(scheme);

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

        verify(schemeService, times(1)).save(any(Scheme.class));
    }

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



}
