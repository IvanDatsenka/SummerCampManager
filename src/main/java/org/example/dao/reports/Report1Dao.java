package org.example.dao.reports;

import org.example.connection.ConnectionToMS;
import org.example.entity.ChildView;
import org.example.entity.views.ReportView;
import org.example.exception.DaoException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Report1Dao {
    private static String SQL_SELECT = "select * from report1";

    public List<ReportView> getView(){
        try(var connection = ConnectionToMS.get();
            var statement = connection.prepareStatement(SQL_SELECT)){

            List<ReportView> reportViews = new ArrayList<>();

            var resultSet = statement.executeQuery();

            while (resultSet.next()){
                reportViews.add(buildView(resultSet));
            }

            return reportViews;
        } catch (SQLException e){
            throw new DaoException(e);
        }
    }

    private ReportView buildView(ResultSet resultSet) throws SQLException {
        return ReportView.builder()
                .count(resultSet.getInt("количество_детей"))
                .name(resultSet.getString("название"))
                .build();
    }
}
