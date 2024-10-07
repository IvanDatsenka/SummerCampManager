package org.example.service.impl;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.dao.view.SquadViewDao;
import org.example.entity.ChildView;
import org.example.entity.views.SquadView;
import org.example.service.SquadViewService;

import java.util.List;
import java.util.Set;

public class SquadViewServiceImpl implements SquadViewService {

    private final SquadViewDao squadViewDao = new SquadViewDao();
    @Override
    public ObservableList<SquadView> getData() {
        return FXCollections.<SquadView>observableArrayList(squadViewDao.getView());
    }
}
