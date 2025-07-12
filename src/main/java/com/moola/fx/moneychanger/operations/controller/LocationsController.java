package com.moola.fx.moneychanger.operations.controller;

import com.moola.fx.moneychanger.operations.dto.LocationDTO;
import com.moola.fx.moneychanger.operations.service.LocationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/locations")
public class LocationsController {

    private final LocationsService locationsService;

    @Autowired
    public LocationsController(LocationsService locationsService) {
        this.locationsService = locationsService;
    }

    @GetMapping
    public ResponseEntity<List<LocationDTO>> list(@RequestParam(value ="countryCode", required = false) String countryCode) {
        List<LocationDTO> locations = null;
        if(countryCode != null && !countryCode.isEmpty()) {
            locations = locationsService.findByCountryCode(countryCode);
        } else {
            locations = locationsService.listAllLocations();
        }

        return ResponseEntity.ok(locations);
    }

}
