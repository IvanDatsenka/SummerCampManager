package org.example.dao.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.connection.ConnectionToMS;
import org.example.dao.EventDao;
import org.example.entity.Squad;
import org.example.entity.views.EventView;
import org.example.entity.views.SquadView;
import org.example.exception.DaoException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EventViewDao {

    private final static String SELECT_SQL = "select * from мероприятие_предст";

    private final EventDao eventDao = EventDao.getInstance();

    public List<EventView> getView(){
        try(var connection = ConnectionToMS.get();
            var statement = connection.prepareStatement(SELECT_SQL)){

            List<EventView> eventViews = new ArrayList<>();

            var resultSet = statement.executeQuery();

            while (resultSet.next()){
                eventViews.add(buildView(resultSet));
            }

            return eventViews;
        } catch (SQLException e){
            throw new DaoException(e);
        }
    }

    private EventView buildView(ResultSet resultSet) throws SQLException {
        return EventView.builder()
                .eventId(resultSet.getLong("код_мероприятия"))
                .employee(resultSet.getString("сотрудник"))
                .eventDate(resultSet.getDate("дата"))
                .employeeId(resultSet.getLong("код_сотрудника"))
                .eventName(resultSet.getString("название"))
                .build();
    }

    public boolean deleteEventFromEventView(EventView eventView){
        return eventDao.delete(eventView.getEventId());
    }
}
