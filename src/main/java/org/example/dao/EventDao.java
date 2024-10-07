package org.example.dao;

import org.example.connection.ConnectionToMS;
import org.example.entity.Employee;
import org.example.entity.Event;
import org.example.exception.DaoException;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class EventDao implements Dao<Long, Event>{

    private static final EventDao INSTANCE = new EventDao();

    private EventDao(){}

    public static EventDao getInstance(){return INSTANCE;}


    private static final String SAVE_SQL = """
            insert into Мероприятие
            (название, дата, код_сотрудника)
            values (?,?,?)
            """;

    private static final String DELETE_SQL = """
            DELETE FROM Мероприятие 
            where код = ?
            """;

    private static final String FIND_ALL_SQL = """
            SELECT код, название, дата, код_сотрудника
            FROM Мероприятие
            """;

    private static final String FIND_BY_ID_SQL = """
            SELECT  код, название, дата, код_сотрудника
            FROM Мероприятие
            where код = ?
            """;

    private static final String UPDATE_SQL = """
            UPDATE Мероприятие
            SET название = ?, дата = ?, код_сотрудника = ?
            where код = ?
            """;


    private static final String GET_ID_AND_NAMES_SQL = """
            select код, название
            from Мероприятие
            """;

    private static final String GET_DATE_AND_NAMES_SQL = """
            select дата, название
            from Мероприятие
            """;
    @Override
    public Event save(Event event) {
        try(var connection = ConnectionToMS.get();
            var statement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS))
        {
            statement.setString(1, event.getName());
            statement.setDate(2, event.getDate());
            statement.setLong(3, event.getEmployeeId());

            statement.executeUpdate();
            var keys = statement.getGeneratedKeys();
            if(keys.next()){
                event.setId(keys.getLong(1));
            }

            return event;
        }catch (SQLException e){
            throw new DaoException(e);
        }
    }

    @Override
    public Optional<Event> findById(Long id) {
        try(var connection = ConnectionToMS.get();
            var statement = connection.prepareStatement(FIND_BY_ID_SQL)){
            statement.setLong(1, id);
            var result = statement.executeQuery();

            Event event = null;

            if(result.next()){
                event = buildEvent(result);
            }

            return Optional.ofNullable(event);
        }catch (SQLException e){
            throw new DaoException(e);
        }
    }

    private Event buildEvent(ResultSet result) throws SQLException {
        return Event.builder()
                .id(result.getLong("код"))
                .name(result.getString("название"))
                .date(result.getDate("дата"))
                .employeeId(result.getLong("код_сотрудника"))
                .build();
    }

    @Override
    public boolean update(Event event) {
        try(var connection = ConnectionToMS.get();
            var statement = connection.prepareStatement(UPDATE_SQL)){
            statement.setString(1, event.getName());
            statement.setDate(2, event.getDate());
            statement.setLong(3, event.getEmployeeId());
            statement.setLong(4, event.getId());
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
    public List<Event> findAll() {
        try(var connection = ConnectionToMS.get();
            var statement = connection.prepareStatement(FIND_ALL_SQL)){

            List<Event> events = new ArrayList<>();

            var resultSet = statement.executeQuery();

            while (resultSet.next()){
                events.add(buildEvent(resultSet));
            }

            return events;
        } catch (SQLException e){
            throw new DaoException(e);
        }
    }

    public HashMap<Date, String> getDateAndNamesHash(){
        HashMap<Date, String> dateAndNames = new HashMap<>();
        try(var connection = ConnectionToMS.get();
            var statement = connection.prepareStatement(GET_ID_AND_NAMES_SQL)) {
            var result = statement.executeQuery();

            while(result.next()) {
                String name = result.getString("название");
                dateAndNames.put(result.getDate("дата"), name);
            }
            return dateAndNames;
        }catch (SQLException e){
            throw new DaoException(e);
        }
    }


    public HashMap<Long, String> getIdAndNames() {

        HashMap<Long, String> idNames = new HashMap<>();
        try(var connection = ConnectionToMS.get();
            var statement = connection.prepareStatement(GET_ID_AND_NAMES_SQL)) {
            var result = statement.executeQuery();

            while(result.next()) {
                String name = result.getString("название");
                idNames.put(result.getLong("код"), name);
            }
            return idNames;
        }catch (SQLException e){
            throw new DaoException(e);
        }
    }
}
