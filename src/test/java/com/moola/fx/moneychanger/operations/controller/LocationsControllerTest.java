package com.moola.fx.moneychanger.operations.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moola.fx.moneychanger.operations.dto.LocationDTO;
import com.moola.fx.moneychanger.operations.service.LocationsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LocationsController.class)
class LocationsControllerTest {

    @Autowired private MockMvc mockMvc;

    @Autowired private ObjectMapper objectMapper;

    @MockitoBean private LocationsService locationsService;

    @Test
    void testListLocations() throws Exception {
        LocationDTO locationDTO1 = new LocationDTO();
        locationDTO1.setId(1);
        locationDTO1.setLocationName("Tampines");
        locationDTO1.setCountryCode("SG");

        LocationDTO locationDTO2 = new LocationDTO();
        locationDTO2.setId(2);
        locationDTO2.setLocationName("Haw Par Villa");
        locationDTO2.setCountryCode("SG");

        LocationDTO locationDTO3 = new LocationDTO();
        locationDTO3.setId(3);
        locationDTO3.setLocationName("Chennai");
        locationDTO3.setCountryCode("IN");

        String countryCode = "SG";

        when(locationsService.listAllLocations()).thenReturn(List.of(locationDTO1, locationDTO2, locationDTO3));
        when(locationsService.findByCountryCode(countryCode)).thenReturn(List.of(locationDTO1, locationDTO2));

        mockMvc.perform(get("/v1/locations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].locationName").value("Tampines"))
                .andExpect(jsonPath("$[0].countryCode").value("SG"))
                .andExpect(jsonPath("$[1].locationName").value("Haw Par Villa"))
                .andExpect(jsonPath("$[1].countryCode").value("SG"))
                .andExpect(jsonPath("$[2].locationName").value("Chennai"))
                .andExpect(jsonPath("$[2].countryCode").value("IN"));
        verify(locationsService).listAllLocations();

        mockMvc.perform(get("/v1/locations?countryCode=" + countryCode))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].locationName").value("Tampines"))
                .andExpect(jsonPath("$[0].countryCode").value("SG"))
                .andExpect(jsonPath("$[1].locationName").value("Haw Par Villa"))
                .andExpect(jsonPath("$[1].countryCode").value("SG"));
        verify(locationsService).findByCountryCode(countryCode);
    }

}
