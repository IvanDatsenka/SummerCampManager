package org.example.dao;

import org.example.connection.ConnectionToMS;
import org.example.entity.Building;
import org.example.exception.DaoException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class BuildingDao implements Dao<Long, Building>{
    private static final BuildingDao INSTANCE = new BuildingDao();

    public static BuildingDao getInstance(){return INSTANCE;}

    private BuildingDao(){}

    private static final String SAVE_SQL = """
            insert into Корпус
            (название)
            values (?)
            """;

    private static final String DELETE_SQL = """
            DELETE FROM Корпус 
            where код = ?
            """;

    private static final String FIND_ALL_SQL = """
            SELECT код, название
            FROM Корпус
            """;

    private static final String FIND_BY_ID_SQL = """
            SELECT код, название
            FROM Корпус
            where код = ?
            """;

    private static final String UPDATE_SQL = """
            UPDATE Корпус
            SET название = ?
            where код = ?
            """;

    private static final String FIND_ID_BY_NAME_SQL = """
            select код from Корпус where название = ?
            """;


    private static final String GET_ID_AND_NAMES_SQL = """
            select код, название 
            from Корпус
            """;

    public HashMap<Long, String> getIdAndNames() {
        HashMap<Long, String> idNames = new HashMap<>();
        try(var connection = ConnectionToMS.get();
            var statement = connection.prepareStatement(GET_ID_AND_NAMES_SQL)) {
            var result = statement.executeQuery();

            while(result.next())
                idNames.put(result.getLong("код"), result.getString("название"));

            return idNames;
        }catch (SQLException e){
            throw new DaoException(e);
        }
    }
    @Override
    public Building save(Building building) {
        try(var connection = ConnectionToMS.get();
            var statement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS))
        {
            statement.setString(1, building.getName());

            statement.executeUpdate();
            var keys = statement.getGeneratedKeys();
            if(keys.next()){
                building.setId(keys.getLong(1));
            }

            return building;
        }catch (SQLException e){
            throw new DaoException(e);
        }
    }

    public Optional<Long> findIdByName(String buildingName) {
        try(var connection = ConnectionToMS.get();
            var statement = connection.prepareStatement(FIND_ID_BY_NAME_SQL)){
            statement.setString(1, buildingName);
            var value = statement.executeQuery();

            Long id = null;
            if(value.next())
                id = value.getLong("код");

            return Optional.ofNullable(id);

        }catch (SQLException e){
            throw new DaoException(e);
        }
    }

    @Override
    public Optional<Building> findById(Long id) {
        try(var connection = ConnectionToMS.get();
            var statement = connection.prepareStatement(FIND_BY_ID_SQL)){
            statement.setLong(1, id);
            var result = statement.executeQuery();

            Building building = null;

            if(result.next()){
                building = buildBuilding(result);
            }

            return Optional.ofNullable(building);
        }catch (SQLException e){
            throw new DaoException(e);
        }
    }

    private Building buildBuilding(ResultSet result) throws SQLException {
        return Building.builder()
                .id(result.getLong("код"))
                .name(result.getString("название"))
                .build();
    }

    @Override
    public boolean update(Building building) {
        try(var connection = ConnectionToMS.get();
            var statement = connection.prepareStatement(UPDATE_SQL)){
            statement.setString(1, building.getName());
            statement.setLong(2, building.getId());
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
    public List<Building> findAll() {
        try(var connection = ConnectionToMS.get();
            var statement = connection.prepareStatement(FIND_ALL_SQL)){

            List<Building> buildings = new ArrayList<>();

            var resultSet = statement.executeQuery();

            while (resultSet.next()){
                buildings.add(buildBuilding(resultSet));
            }

            return buildings;
        } catch (SQLException e){
            throw new DaoException(e);
        }
    }
}
