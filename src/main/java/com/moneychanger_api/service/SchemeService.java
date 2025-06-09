package com.moneychanger_api.service;

import com.moneychanger_api.model.Scheme;

import java.util.List;

public interface SchemeService {
    List<Scheme> listAll();
    Scheme get(Integer id);
    Scheme save(Scheme item);
    void delete(Integer id, Integer who);


}
