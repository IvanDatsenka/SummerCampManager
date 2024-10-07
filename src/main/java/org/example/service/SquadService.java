package org.example.service;

import org.example.entity.Squad;

import java.util.List;
import java.util.Optional;

public interface SquadService {
    public List<Squad> getAllSquads();
    Optional<Long> getIdByName(String name);
    List<String> getSquadNames();

    Squad save(Squad squad);
}
