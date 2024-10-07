package org.example.dao.view;

import org.example.connection.ConnectionToMS;
import org.example.dao.EmployeeDao;
import org.example.entity.views.EmployeeView;
import org.example.entity.views.EventView;
import org.example.exception.DaoException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeViewDao {

    private final static String SELECT = "select * from сотрудники_предст";

    private final EmployeeDao employeeDao = EmployeeDao.getInstance();

    public List<EmployeeView> getView(){
        try(var connection = ConnectionToMS.get();
            var statement = connection.prepareStatement(SELECT)){

            List<EmployeeView> employeeViews = new ArrayList<>();

            var resultSet = statement.executeQuery();

            while (resultSet.next()){
                employeeViews.add(buildView(resultSet));
            }

            return employeeViews;
        } catch (SQLException e){
            throw new DaoException(e);
        }
    }

    private EmployeeView buildView(ResultSet resultSet) throws SQLException {
        return EmployeeView.builder()
                .employeeId(resultSet.getLong("код_сотрудника"))
                .jobTitleId(resultSet.getLong("код_должности"))
                .jobTitleName(resultSet.getString("название_должности"))
                .employeeFullName(resultSet.getString("сотрудник"))
                .build();
    }

    public boolean deleteEventFromEventView(EmployeeView employeeView){
        return employeeDao.delete(employeeView.getEmployeeId());
    }
}
