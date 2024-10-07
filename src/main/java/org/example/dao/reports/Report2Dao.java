package org.example.dao.reports;

import org.example.connection.ConnectionToMS;
import org.example.entity.views.Report2View;
import org.example.entity.views.ReportView;
import org.example.exception.DaoException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Report2Dao {
    private final static String SQL_PROC_EXEC = "exec тип_отчет_мерориятия ?";

    public String wrapInSingleQuotes(String input) {
        return "'" + input + "'";
    }
    public List<Report2View> getReport2Views(String evenName){
        String correctName = wrapInSingleQuotes(evenName);
        try(var connection = ConnectionToMS.get();
            var statement = connection.prepareStatement(SQL_PROC_EXEC)){
            List<Report2View> reportViews = new ArrayList<>();
            statement.setString(1, evenName);
            var resultSet = statement.executeQuery();

            while (resultSet.next()){
                reportViews.add(buildView(resultSet));
            }

            return reportViews;
        } catch (SQLException e){
            throw new DaoException(e);
        }
    }

    private Report2View buildView(ResultSet resultSet) throws SQLException {
        return Report2View.builder()
                .eventName(resultSet.getString("название"))
                .squadName(resultSet.getString("название_отряда"))
                .employeeName(resultSet.getString("сотрудник"))
                .build();
    }
}
