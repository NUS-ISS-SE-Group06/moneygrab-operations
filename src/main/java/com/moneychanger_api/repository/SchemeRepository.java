package com.moneychanger_api.repository;

import com.moneychanger_api.model.Scheme;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchemeRepository extends JpaRepository<Scheme, Integer> {}
