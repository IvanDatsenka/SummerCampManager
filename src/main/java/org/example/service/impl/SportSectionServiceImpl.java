package org.example.service.impl;

import org.example.dao.Dao;
import org.example.dao.SportSectionDao;
import org.example.entity.SportSection;
import org.example.service.SportSectionService;

import java.util.List;
import java.util.Optional;

public class SportSectionServiceImpl implements SportSectionService {
    private final SportSectionDao sportSectionDao = SportSectionDao.getInstance();
    @Override
    public List<SportSection> getAllSportSections() {
        return sportSectionDao.findAll();
    }
    @Override
    public Optional<Long> findIdByName(String name) {
        return sportSectionDao.findIdByName(name);
    }
}
