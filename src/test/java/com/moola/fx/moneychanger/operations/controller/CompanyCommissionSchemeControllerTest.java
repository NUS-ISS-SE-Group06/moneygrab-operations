package com.moola.fx.moneychanger.operations.controller;

import com.moola.fx.moneychanger.operations.dto.CompanyCommissionSchemeDTO;
import com.moola.fx.moneychanger.operations.mapper.CompanyCommissionSchemeMapper;
import com.moola.fx.moneychanger.operations.model.CompanyCommissionScheme;
import com.moola.fx.moneychanger.operations.model.Scheme;
import com.moola.fx.moneychanger.operations.service.CompanyCommissionSchemeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompanyCommissionSchemeControllerTest {

    @Mock
    private CompanyCommissionSchemeService service;

    @Mock
    private CompanyCommissionSchemeMapper mapper;

    @InjectMocks
    private CompanyCommissionSchemeController controller;

    @Test
    void list_withoutSchemeId_returnsAll() {
        CompanyCommissionScheme entity1 = new CompanyCommissionScheme();
        CompanyCommissionScheme entity2 = new CompanyCommissionScheme();
        CompanyCommissionSchemeDTO dto1 = new CompanyCommissionSchemeDTO();
        CompanyCommissionSchemeDTO dto2 = new CompanyCommissionSchemeDTO();

        when(service.listAll()).thenReturn(List.of(entity1, entity2));
        when(mapper.toDTO(entity1)).thenReturn(dto1);
        when(mapper.toDTO(entity2)).thenReturn(dto2);

        ResponseEntity<List<CompanyCommissionSchemeDTO>> response = controller.list(null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<CompanyCommissionSchemeDTO> body = response.getBody();
        assertNotNull(body);
        assertEquals(2, body.size());
        assertEquals(List.of(dto1, dto2), body);

        verify(service).listAll();
        verify(service, never()).findBySchemeId(any());
    }

    @Test
    void list_withSchemeId_returnsFiltered() {
        int schemeId = 42;
        CompanyCommissionScheme entity = new CompanyCommissionScheme();
        CompanyCommissionSchemeDTO dto = new CompanyCommissionSchemeDTO();

        when(service.findBySchemeId(schemeId)).thenReturn(List.of(entity));
        when(mapper.toDTO(entity)).thenReturn(dto);

        ResponseEntity<List<CompanyCommissionSchemeDTO>> response = controller.list(schemeId);

        assertEquals(200, response.getStatusCodeValue());
        List<CompanyCommissionSchemeDTO> body = response.getBody();
        assertNotNull(body);
        assertEquals(1, body.size());
        assertSame(dto, body.get(0));

        verify(service).findBySchemeId(schemeId);
        verify(service, never()).listAll();
    }

    @Test
    void get_existingId_returnsDto() {
        int id = 1;
        CompanyCommissionScheme entity = new CompanyCommissionScheme();
        CompanyCommissionSchemeDTO dto = new CompanyCommissionSchemeDTO();

        when(service.get(id)).thenReturn(entity);
        when(mapper.toDTO(entity)).thenReturn(dto);

        ResponseEntity<CompanyCommissionSchemeDTO> response = controller.get(id);

        assertEquals(200, response.getStatusCodeValue());
        assertSame(dto, response.getBody());

        verify(service).get(id);
    }

    @Test
    void create_validDto_returnsSavedDto() {
        CompanyCommissionSchemeDTO inputDto = new CompanyCommissionSchemeDTO();
        inputDto.setCreatedBy(7);

        CompanyCommissionScheme entity = new CompanyCommissionScheme();
        CompanyCommissionScheme saved = new CompanyCommissionScheme();
        CompanyCommissionSchemeDTO outputDto = new CompanyCommissionSchemeDTO();

        when(mapper.toEntity(inputDto)).thenReturn(entity);
        when(service.save(any(CompanyCommissionScheme.class))).thenReturn(saved);
        when(mapper.toDTO(saved)).thenReturn(outputDto);

        ResponseEntity<CompanyCommissionSchemeDTO> response = controller.create(inputDto);

        assertEquals(200, response.getStatusCodeValue());
        assertSame(outputDto, response.getBody());

        ArgumentCaptor<CompanyCommissionScheme> captor = ArgumentCaptor.forClass(CompanyCommissionScheme.class);
        verify(service).save(captor.capture());
        assertEquals(inputDto.getCreatedBy(), captor.getValue().getUpdatedBy());
    }

    @Test
    void update_existingDto_returnsUpdatedDto() {
        int id = 3;

        CompanyCommissionSchemeDTO inputDto = new CompanyCommissionSchemeDTO();
        inputDto.setId(id);
        inputDto.setSchemeId(100);
        inputDto.setNameTag("NewTag");
        inputDto.setUpdatedBy(8);

        CompanyCommissionScheme updated = new CompanyCommissionScheme();
        CompanyCommissionSchemeDTO outputDto = new CompanyCommissionSchemeDTO();

        when(service.save(inputDto)).thenReturn(updated);
        when(mapper.toDTO(updated)).thenReturn(outputDto);

        ResponseEntity<CompanyCommissionSchemeDTO> response = controller.update(id, inputDto);

        assertEquals(200, response.getStatusCodeValue());
        assertSame(outputDto, response.getBody());

        verify(service).save(inputDto);
        verify(mapper).toDTO(updated);
    }


    @Test
    void update_withNullSchemeId_onlyUpdatesUser() {
        int id = 4;

        CompanyCommissionSchemeDTO inputDto = new CompanyCommissionSchemeDTO();
        inputDto.setId(id);
        inputDto.setUpdatedBy(88);  // schemeId is null

        CompanyCommissionScheme updated = new CompanyCommissionScheme();
        CompanyCommissionSchemeDTO outputDto = new CompanyCommissionSchemeDTO();

        when(service.save(inputDto)).thenReturn(updated);
        when(mapper.toDTO(updated)).thenReturn(outputDto);

        ResponseEntity<CompanyCommissionSchemeDTO> response = controller.update(id, inputDto);

        assertEquals(200, response.getStatusCodeValue());
        assertSame(outputDto, response.getBody());

        verify(service).save(inputDto);
        verify(mapper).toDTO(updated);
    }


    @Test
    void delete_validId_invokesServiceAndReturnsNoContent() {
        int id = 5;
        int userId = 9;

        doNothing().when(service).delete(id, userId);

        ResponseEntity<Void> response = controller.delete(id, userId);

        assertEquals(204, response.getStatusCodeValue());
        assertNull(response.getBody());

        verify(service).delete(id, userId);
    }
}
