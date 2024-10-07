package org.example.service.impl;

import org.example.dao.ShiftDao;
import org.example.entity.Shift;
import org.example.service.ShiftService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShiftServiceImpl implements ShiftService {
    private final ShiftDao shiftDao = ShiftDao.getInstance();
    @Override
    public List<String> getShiftNames() {
        List<Shift> shifts = shiftDao.findAll();
        List<String> names = new ArrayList<>();

        for (Shift shift:shifts) {
            names.add(shift.getName());
        }

        return names;
    }

    public HashMap<Long, String> getIdAndNames(){
        return shiftDao.getIdAndName();
    }
}
