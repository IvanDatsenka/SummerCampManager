package org.example.service.impl;

import org.example.dao.EmployeeDao;
import org.example.entity.Employee;
import org.example.entity.Shift;
import org.example.service.EmployeeService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeDao employeeDao = EmployeeDao.getInstance();
    @Override
    public List<String> getNames() {
        List<Employee> employees = employeeDao.findAll();
        List<String> names = new ArrayList<>();

        for (Employee employee:employees) {
            names.add(employee.getFirstName() + " " + employee.getSecondName());
        }

        return names;
    }

    @Override
    public HashMap<Long, String> getIdAndNames() {
        return employeeDao.getIdAndNames();
    }
}
