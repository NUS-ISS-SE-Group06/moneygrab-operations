package com.moola.fx.moneychanger.operations.service;

import com.moola.fx.moneychanger.operations.model.Scheme;

import java.util.List;

public interface SchemeService {
    List<Scheme> listAll();
    Scheme get(Integer id);
    Scheme save(Scheme entity);
    void delete(Integer id, Integer userId);


}
