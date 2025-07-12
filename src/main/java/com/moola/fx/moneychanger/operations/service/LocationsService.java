package com.moola.fx.moneychanger.operations.service;

import com.moola.fx.moneychanger.operations.model.Locations;

import java.util.List;

public interface LocationsService {
    List<String> listAllLocations();

    List<Locations> findByCountryCode(String countryCode);
}
