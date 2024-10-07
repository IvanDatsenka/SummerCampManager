package org.example.dao.view;

import org.example.connection.ConnectionToMS;
import org.example.dao.RoomDao;
import org.example.entity.views.ParticipateView;
import org.example.entity.views.RoomView;
import org.example.exception.DaoException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoomViewDao {
    private static final String SELECT_SQL = "select * from room_view_kurs";

    private final RoomDao roomDao = RoomDao.getInstance();

    public List<RoomView> getView(){
        try(var connection = ConnectionToMS.get();
            var statement = connection.prepareStatement(SELECT_SQL)){

            List<RoomView> participateViews = new ArrayList<>();

            var resultSet = statement.executeQuery();

            while (resultSet.next()){
                participateViews.add(buildView(resultSet));
            }

            return participateViews;
        } catch (SQLException e){
            throw new DaoException(e);
        }
    }
    private RoomView buildView(ResultSet resultSet) throws SQLException {
        return RoomView.builder()
                .roomId(resultSet.getLong("код_комнаты"))
                .buildingId(resultSet.getLong("код_корпуса"))
                .roomNumber(resultSet.getString("номер"))
                .buildingNumber(resultSet.getString("название_корпуса"))
                .build();
    }

    public boolean deleteRoomView(RoomView roomView) {
        return roomDao.delete(roomView.getRoomId());
    }
}
