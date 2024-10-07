package org.example.service;

import java.util.HashMap;
import java.util.List;

public interface EmployeeService {
    public List<String> getNames();
    HashMap<Long, String> getIdAndNames();
}
