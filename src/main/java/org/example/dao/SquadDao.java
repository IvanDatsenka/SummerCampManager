package org.example.dao;

import org.example.connection.ConnectionToMS;
import org.example.entity.Child;
import org.example.entity.Squad;
import org.example.exception.DaoException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class SquadDao implements Dao<Long, Squad>{
    private final static SquadDao INSTANCE = new SquadDao();

    private static final String SAVE_SQL = """
            insert into Отряд
            (код_сотрудника, код_смены, название, код_корпуса) 
            values (?,?,?,?)
            """;

    private static final String DELETE_SQL = """
            DELETE FROM Отряд 
            where код = ?
            """;

    private static final String FIND_ALL_SQL = """
            SELECT код, код_сотрудника, код_смены, название, код_корпуса 
            FROM Отряд
            """;

    private static final String FIND_BY_ID_SQL = """
            SELECT код, код_сотрудника, код_смены, название, код_корпуса 
            FROM Отряд
            where код = ?
            """;

    private static final String UPDATE_SQL = """
            UPDATE Отряд
            SET код_сотрудника = ?, код_смены = ?, название = ?, код_корпуса = ?
            where код = ?
            """;

    private static final String FIND_ID_BY_NAME_SQL = """
            select код from Отряд where название = ?
            """;

    private static final String GET_ID_AND_NAMES_SQL = """
            select код, название
            from Отряд
            """;
    private SquadDao(){
    }

    public static SquadDao getInstance(){return INSTANCE;}

    @Override
    public Squad save(Squad squad) {
        try(var connection = ConnectionToMS.get();
            var statement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS))
        {
            statement.setLong(1, squad.getEmployeeId());
            statement.setLong(2, squad.getShiftId());
            statement.setString(3, squad.getName());
            statement.setLong(4, squad.getBuildingId());

            statement.executeUpdate();
            var keys = statement.getGeneratedKeys();
            if(keys.next()){
                squad.setId(keys.getLong(1));
            }

            return squad;
        }catch (SQLException e){
            throw new DaoException(e);
        }
    }

    @Override
    public Optional<Squad> findById(Long id) {
        try(var connection = ConnectionToMS.get();
            var statement = connection.prepareStatement(FIND_BY_ID_SQL)){
            statement.setLong(1, id);
            var result = statement.executeQuery();

            Squad squad = null;

            if(result.next()){
                squad = buildSquad(result);
            }

            return Optional.ofNullable(squad);
        }catch (SQLException e){
            throw new DaoException(e);
        }
    }

    @Override
    public boolean update(Squad squad) {
        try(var connection = ConnectionToMS.get();
            var statement = connection.prepareStatement(UPDATE_SQL)){
            statement.setLong(1, squad.getEmployeeId());
            statement.setLong(2, squad.getShiftId());
            statement.setString(3, squad.getName());
            statement.setLong(4, squad.getBuildingId());
            statement.setLong(5, squad.getId());

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
    public List<Squad> findAll() {
        try(var connection = ConnectionToMS.get();
            var statement = connection.prepareStatement(FIND_ALL_SQL)){

            List<Squad> squads = new ArrayList<>();

            var resultSet = statement.executeQuery();

            while (resultSet.next()){
                squads.add(buildSquad(resultSet));
            }

            return squads;
        } catch (SQLException e){
            throw new DaoException(e);
        }
    }

    private static Squad buildSquad(ResultSet result) throws SQLException {
        return Squad.builder()
                .id(result.getLong("код"))
                .employeeId(result.getLong("код_сотрудника"))
                .shiftId(result.getLong("код_смены"))
                .name(result.getString("название"))
                .buildingId(result.getLong("код_корпуса"))
                .build();
    }

    public Optional<Long> findIdByName(String squadName) {
        try(var connection = ConnectionToMS.get();
            var statement = connection.prepareStatement(FIND_ID_BY_NAME_SQL)){
            statement.setString(1, squadName);
            var value = statement.executeQuery();

            Long id = null;
            if(value.next())
                id = value.getLong("код");

            return Optional.ofNullable(id);

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
