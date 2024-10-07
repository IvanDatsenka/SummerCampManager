package org.example.service;

import java.util.HashMap;
import java.util.List;

public interface BuildingService {
    public List<String> getNames();

    HashMap<Long, String> getIdAndNames();

}
