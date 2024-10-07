package org.example.dao.view;

import org.example.connection.ConnectionToMS;
import org.example.dao.SquadDao;
import org.example.entity.ChildView;
import org.example.entity.Squad;
import org.example.entity.views.SquadView;
import org.example.exception.DaoException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SquadViewDao {
    private final static String SELECT = "select * from отряды_представление";

    private final SquadDao squadDao = SquadDao.getInstance();

    public List<SquadView> getView(){
        try(var connection = ConnectionToMS.get();
            var statement = connection.prepareStatement(SELECT)){

            List<SquadView> squadViews = new ArrayList<>();

            var resultSet = statement.executeQuery();

            while (resultSet.next()){
                squadViews.add(buildView(resultSet));
            }

            return squadViews;
        } catch (SQLException e){
            throw new DaoException(e);
        }
    }
    private SquadView buildView(ResultSet resultSet) throws SQLException {
        return SquadView.builder()
                .squadId(resultSet.getLong("код_отряда"))
                .buildingId(resultSet.getLong("код_корпуса"))
                .employeeId(resultSet.getLong("код_сотрдуника"))
                .shiftId(resultSet.getLong("код_смены"))
                .squadName(resultSet.getString("название"))
                .buildingName(resultSet.getString("корпус"))
                .counselor(resultSet.getString("Вожатый"))
                .shiftName(resultSet.getString("смена"))
                .build();
    }

    public boolean deleteSquadFromSquadDao(SquadView squadView) {
        return squadDao.delete(squadView.getSquadId());
    }
}
