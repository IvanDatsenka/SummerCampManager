package org.example.service.impl;

import org.example.dao.*;
import org.example.entity.Shift;
import org.example.entity.Squad;
import org.example.service.EmployeeService;
import org.example.service.SquadService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SquadServiceImpl implements SquadService {
    private final SquadDao squadDao = SquadDao.getInstance();

    private final EmployeeDao employeeDao = EmployeeDao.getInstance();
    private final BuildingDao buildingDao = BuildingDao.getInstance();
    private final ShiftDao shiftDao = ShiftDao.getInstance();
    @Override
    public List<Squad> getAllSquads() {
        return squadDao.findAll();
    }
    public Optional<Long> getIdByName(String name){
        return squadDao.findIdByName(name);
    }

    @Override
    public List<String> getSquadNames() {
        List<Squad> squads = squadDao.findAll();
        List<String> names = new ArrayList<>();

        for (Squad squad:squads) {
            names.add(squad.getName());
        }

        return names;
    }

    @Override
    public Squad save(Squad squad) {
        return squadDao.save(squad);
    }
}
