package org.example.dao.view;

import org.apache.poi.poifs.property.Child;
import org.example.connection.ConnectionToMS;
import org.example.dao.ChildDao;
import org.example.entity.ChildView;
import org.example.exception.DaoException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AllChildViewDao {

    private final static String SELECT = "select * from все_дети_в_лагере";

    private final ChildDao childDao = ChildDao.getInstance();

    public List<ChildView> getView(){
        try(var connection = ConnectionToMS.get();
            var statement = connection.prepareStatement(SELECT)){

            List<ChildView> childViews = new ArrayList<>();

            var resultSet = statement.executeQuery();

            while (resultSet.next()){
                childViews.add(buildView(resultSet));
            }

            return childViews;
        } catch (SQLException e){
            throw new DaoException(e);
        }
    }

    public boolean deleteChildFromChildView(ChildView childView){
        return childDao.delete(childView.getChildId());
    }

    private ChildView buildView(ResultSet resultSet) throws SQLException {
        return ChildView.builder()
                .childId(resultSet.getLong("код_ребёнка"))
                .firstName(resultSet.getString("имя"))
                .secondName(resultSet.getString("фамилия"))
                .squadName(resultSet.getString("отряд"))
                .sportSectionName(resultSet.getString("название"))
                .roomNumber(resultSet.getString("номер")+" "+resultSet.getString("корпус"))

                .build();
    }
}
