package org.example.service;

import org.example.entity.SportSection;

import java.util.List;
import java.util.Optional;

public interface SportSectionService {
    List<SportSection> getAllSportSections();
    Optional<Long> findIdByName(String name);
 }
