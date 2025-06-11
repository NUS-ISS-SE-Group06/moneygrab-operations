package com.moola.fx.moneychanger.operations.repository;

import com.moola.fx.moneychanger.operations.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
}