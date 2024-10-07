package org.example.service.impl;

import org.example.dao.ChildDao;
import org.example.dao.Dao;
import org.example.entity.Child;
import org.example.service.ChildTableViewTableService;

import java.util.List;

public class ChildTableViewTableServiceImpl implements ChildTableViewTableService {
    private final Dao dao = ChildDao.getInstance();

    @Override
    public List<Child> getChild() {
        return dao.findAll();
    }
}
