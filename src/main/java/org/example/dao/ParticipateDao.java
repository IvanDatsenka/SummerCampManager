package org.example.dao;

import org.example.connection.ConnectionToMS;
import org.example.entity.JobTitle;
import org.example.entity.Participate;
import org.example.exception.DaoException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ParticipateDao implements Dao<Long, Participate>{

    private static final ParticipateDao INSTANCE = new ParticipateDao();

    private ParticipateDao(){}
    public static ParticipateDao getInstance(){return INSTANCE;}

    private static final String SAVE_SQL = """
            insert into Участие
            (код_мероприятия, код_отряда) 
            values (?,?)
            """;

    private static final String DELETE_SQL = """
            DELETE FROM Участие 
            where код = ?
            """;

    private static final String FIND_ALL_SQL = """
            SELECT код, код_мероприятия, код_отряда
            FROM Участие
            """;

    private static final String FIND_BY_ID_SQL = """
            SELECT код, код_мероприятия, код_отряда
            FROM Участие
            where код = ?
            """;

    private static final String UPDATE_SQL = """
            UPDATE Участие
            SET код_мероприятия = ?, код_отряда = ?
            where код = ?
            """;
    @Override
    public Participate save(Participate participate) {
        try(var connection = ConnectionToMS.get();
            var statement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS))
        {
            statement.setLong(1, participate.getEventId());
            statement.setLong(2, participate.getSquadId());

            statement.executeUpdate();
            var keys = statement.getGeneratedKeys();
            if(keys.next()){
                participate.setId(keys.getLong(1));
            }

            return participate;
        }catch (SQLException e){
            throw new DaoException(e);
        }
    }

    @Override
    public Optional<Participate> findById(Long id) {
        try(var connection = ConnectionToMS.get();
            var statement = connection.prepareStatement(FIND_BY_ID_SQL)){
            statement.setLong(1, id);
            var result = statement.executeQuery();

            Participate participate = null;

            if(result.next()){
                participate = buildParticipate(result);
            }

            return Optional.ofNullable(participate);
        }catch (SQLException e){
            throw new DaoException(e);
        }
    }

    private Participate buildParticipate(ResultSet result) throws SQLException {
        return Participate.builder()
                .id(result.getLong("код"))
                .eventId(result.getLong("код_мероприятия"))
                .squadId(result.getLong("код_отряда"))
                .build();
    }

    @Override
    public boolean update(Participate participate) {
        try(var connection = ConnectionToMS.get();
            var statement = connection.prepareStatement(UPDATE_SQL)){
            statement.setLong(1, participate.getEventId());
            statement.setLong(2, participate.getSquadId());
            statement.setLong(3, participate.getId());
            return statement.executeUpdate() > 0;
        } catch (SQLException e){
            throw new DaoException(e);
        }
    }

    @Override
    public boolean delete(Long id) {
        try(var connection = ConnectionToMS.get();
            var statement = connection.prepareStatement(DELETE_SQL)){
            statement.setLong(1, id);

            return statement.executeUpdate() > 0;
        } catch (SQLException e){
            throw new DaoException(e);
        }
    }

    @Override
    public List<Participate> findAll() {
        try (var connection = ConnectionToMS.get();
             var statement = connection.prepareStatement(FIND_ALL_SQL)) {

            List<Participate> participates = new ArrayList<>();

            var resultSet = statement.executeQuery();

            while (resultSet.next()) {
                participates.add(buildParticipate(resultSet));
            }

            return participates;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }
}
