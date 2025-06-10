package com.moneychanger_api.repository;

import com.moneychanger_api.model.Scheme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

public interface SchemeRepository extends JpaRepository<Scheme, Integer> {
    @Modifying
    @Transactional
    @Query("UPDATE Scheme s SET s.isDeleted = true, s.updatedBy = :who, s.updatedAt = CURRENT_TIMESTAMP WHERE s.id = :id")
    int markDeletedById(Integer id, Integer who);

}
