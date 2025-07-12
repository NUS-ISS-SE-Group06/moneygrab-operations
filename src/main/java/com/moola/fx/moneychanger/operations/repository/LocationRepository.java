package com.moola.fx.moneychanger.operations.repository;

import com.moola.fx.moneychanger.operations.model.Locations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationRepository extends JpaRepository<Locations, String>{
    List<Locations> findByCountryCodeAndIsDeletedFalse(String countryCode);
}
