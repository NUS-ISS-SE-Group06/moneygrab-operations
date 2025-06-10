package com.moola.fx.moneychanger.operations.repository;

import com.moola.fx.moneychanger.operations.model.Scheme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface SchemeRepository extends JpaRepository<Scheme, Integer> {
    @Modifying
    @Transactional
    @Query("UPDATE Scheme s SET s.isDeleted = true, s.updatedBy = :who, s.updatedAt = CURRENT_TIMESTAMP WHERE s.id = :id")
    int markDeletedById(Integer id, Integer who);

}
