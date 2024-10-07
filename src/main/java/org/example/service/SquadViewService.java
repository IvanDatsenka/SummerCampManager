package org.example.service;

import javafx.collections.ObservableList;
import org.example.entity.ChildView;
import org.example.entity.views.SquadView;

import java.util.List;
import java.util.Set;

public interface SquadViewService {
    ObservableList<SquadView> getData();
}
