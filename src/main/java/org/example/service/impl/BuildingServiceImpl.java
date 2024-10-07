package org.example.service.impl;

import org.example.dao.BuildingDao;
import org.example.entity.Building;
import org.example.entity.Squad;
import org.example.service.BuildingService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BuildingServiceImpl implements BuildingService {
    private final BuildingDao buildingDao = BuildingDao.getInstance();

    @Override
    public List<String> getNames() {
        List<Building> buildings = buildingDao.findAll();
        List<String> names = new ArrayList<>();

        for (Building building:buildings) {
            names.add(building.getName());
        }

        return names;
    }

    @Override
    public HashMap<Long, String> getIdAndNames() {
        return buildingDao.getIdAndNames();
    }
}
