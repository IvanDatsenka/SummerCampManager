package org.example.dao.view;

import org.example.connection.ConnectionToMS;
import org.example.dao.ParticipateDao;
import org.example.dao.SquadDao;
import org.example.entity.views.ParticipateView;
import org.example.entity.views.SquadView;
import org.example.exception.DaoException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ParticipateViewDao {
    private final static String SELECT = "select * from participate_kurs";

    private final ParticipateDao participateDao = ParticipateDao.getInstance();

    public List<ParticipateView> getView(){
        try(var connection = ConnectionToMS.get();
            var statement = connection.prepareStatement(SELECT)){

            List<ParticipateView> participateViews = new ArrayList<>();

            var resultSet = statement.executeQuery();

            while (resultSet.next()){
                participateViews.add(buildView(resultSet));
            }

            return participateViews;
        } catch (SQLException e){
            throw new DaoException(e);
        }
    }
    private ParticipateView buildView(ResultSet resultSet) throws SQLException {
        return ParticipateView.builder()
                .participateId(resultSet.getLong("код_участия"))
                .squadName(resultSet.getString("название_отряда"))
                .squadId(resultSet.getLong("код_отряда"))
                .eventDate(resultSet.getDate("дата_мероприятия"))
                .eventName(resultSet.getString("название_мероприятия"))
                .eventId(resultSet.getLong("код_мероприятия"))
                .build();
    }

    public boolean deleteParticipateFromParticipateView(ParticipateView participateView) {
        return participateDao.delete(participateView.getParticipateId());
    }
}
