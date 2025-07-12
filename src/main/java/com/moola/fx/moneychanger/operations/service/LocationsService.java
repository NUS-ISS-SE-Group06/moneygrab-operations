package com.moola.fx.moneychanger.operations.service;

import com.moola.fx.moneychanger.operations.dto.LocationDTO;
import com.moola.fx.moneychanger.operations.model.Locations;

import java.util.List;

public interface LocationsService {
    List<LocationDTO> listAllLocations();

    List<LocationDTO> findByCountryCode(String countryCode);
}
