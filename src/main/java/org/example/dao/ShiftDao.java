package org.example.dao;

import org.example.connection.ConnectionToMS;
import org.example.entity.Event;
import org.example.entity.Shift;
import org.example.exception.DaoException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class ShiftDao implements Dao<Long, Shift> {
    private static final ShiftDao INSTANCE = new ShiftDao();

    private ShiftDao(){}
    public static ShiftDao getInstance(){return INSTANCE;}


    private static final String SAVE_SQL = """
            insert into Смена
            (дата, название)
            values (?,?)
            """;

    private static final String DELETE_SQL = """
            DELETE FROM Смена 
            where код = ?
            """;

    private static final String FIND_ALL_SQL = """
            SELECT код, дата, название
            FROM Смена
            """;

    private static final String FIND_BY_ID_SQL = """
            SELECT  код, дата, название
            FROM Смена
            where код = ?
            """;

    private static final String UPDATE_SQL = """
            UPDATE Смена
            SET дата = ?, название = ?
            where код = ?
            """;


    private static final String GET_ID_AND_NAME_SQL = """
            select код, название
            from Смена
            """;

    public HashMap<Long, String> getIdAndName() {
        HashMap<Long, String> idNames = new HashMap<>();
        try(var connection = ConnectionToMS.get();
            var statement = connection.prepareStatement(GET_ID_AND_NAME_SQL)) {
            var result = statement.executeQuery();

            while(result.next())
                idNames.put(result.getLong("код"), result.getString("название"));

            return idNames;
        }catch (SQLException e){
            throw new DaoException(e);
        }
    }

    @Override
    public Shift save(Shift shift) {
        try(var connection = ConnectionToMS.get();
            var statement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS))
        {
            statement.setDate(1, shift.getDate());
            statement.setString(2, shift.getName());

            statement.executeUpdate();
            var keys = statement.getGeneratedKeys();
            if(keys.next()){
                shift.setId(keys.getLong(1));
            }

            return shift;
        }catch (SQLException e){
            throw new DaoException(e);
        }
    }

    @Override
    public Optional<Shift> findById(Long id) {
        try(var connection = ConnectionToMS.get();
            var statement = connection.prepareStatement(FIND_BY_ID_SQL)){
            statement.setLong(1, id);
            var result = statement.executeQuery();

            Shift shift = null;

            if(result.next()){
                shift = buildShift(result);
            }

            return Optional.ofNullable(shift);
        }catch (SQLException e){
            throw new DaoException(e);
        }
    }

    private Shift buildShift(ResultSet result) throws SQLException {
        return Shift.builder()
                .id(result.getLong("код"))
                .date(result.getDate("дата"))
                .name(result.getString("название"))
                .build();
    }


    @Override
    public boolean update(Shift shift) {
        try(var connection = ConnectionToMS.get();
            var statement = connection.prepareStatement(UPDATE_SQL)){
            statement.setDate(1, shift.getDate());
            statement.setString(2, shift.getName());
            statement.setLong(3, shift.getId());
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
    public List<Shift> findAll() {
        try(var connection = ConnectionToMS.get();
            var statement = connection.prepareStatement(FIND_ALL_SQL)){

            List<Shift> shifts = new ArrayList<>();

            var resultSet = statement.executeQuery();

            while (resultSet.next()){
                shifts.add(buildShift(resultSet));
            }

            return shifts;
        } catch (SQLException e){
            throw new DaoException(e);
        }
    }
}
