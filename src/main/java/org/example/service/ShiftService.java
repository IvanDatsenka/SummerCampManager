package org.example.service;

import java.util.HashMap;
import java.util.List;

public interface ShiftService {
    List<String> getShiftNames();
    HashMap<Long, String> getIdAndNames();
}
