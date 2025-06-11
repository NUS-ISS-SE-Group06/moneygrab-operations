package com.moola.fx.moneychanger.operations.repository;

import com.moola.fx.moneychanger.operations.model.Scheme;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SchemeRepository extends JpaRepository<Scheme, Integer> {
    Optional<Scheme> findByIdAndIsDeletedFalse(Integer id);
    boolean existsByNameTagIgnoreCaseAndIsDeletedFalse(String nameTag);
    boolean existsByNameTagIgnoreCaseAndIdNotAndIsDeletedFalse(String nameTag, Integer id);
}