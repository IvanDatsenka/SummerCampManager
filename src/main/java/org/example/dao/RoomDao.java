package org.example.dao;

import org.example.connection.ConnectionToMS;
import org.example.entity.Participate;
import org.example.entity.Room;
import org.example.exception.DaoException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RoomDao implements Dao<Long, Room>{
    private static final RoomDao INSTANCE = new RoomDao();
    private RoomDao(){}
    public static RoomDao getInstance(){return INSTANCE;}
    private static final String SAVE_SQL = """
            insert into Комната
            (код_корпуса, номер) 
            values (?,?)
            """;

    private static final String DELETE_SQL = """
            DELETE FROM Комната 
            where код = ?
            """;

    private static final String FIND_ALL_SQL = """
            SELECT код, код_корпуса, номер
            FROM Комната
            """;

    private static final String FIND_BY_ID_SQL = """
            SELECT код, код_корпуса, номер
            FROM Комната
            where код = ?
            """;

    private static final String UPDATE_SQL = """
            UPDATE Комната
            SET код_корпуса = ?, номер = ?
            where код = ?
            """;

    private static final String FIND_ID_BY_NAME_SQL = """
            select код from Комната where номер = ?
            """;
    @Override
    public Room save(Room room) {
        try(var connection = ConnectionToMS.get();
            var statement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS))
        {
            statement.setLong(1, room.getBuildingId());
            statement.setString(2, room.getNumber());

            statement.executeUpdate();
            var keys = statement.getGeneratedKeys();
            if(keys.next()){
                room.setId(keys.getLong(1));
            }

            return room;
        }catch (SQLException e){
            throw new DaoException(e);
        }
    }

    public Optional<Long> findIdByName(String name){
        try(var connection = ConnectionToMS.get();
            var statement = connection.prepareStatement(FIND_ID_BY_NAME_SQL)){
            statement.setString(1, name);
            var value = statement.executeQuery();

            Long id = null;
            if(value.next())
                id = value.getLong(1);

            return Optional.ofNullable(id);

        }catch (SQLException e){
            throw new DaoException(e);
        }
    }
    @Override
    public Optional<Room> findById(Long id) {
        try(var connection = ConnectionToMS.get();
            var statement = connection.prepareStatement(FIND_BY_ID_SQL)){
            statement.setLong(1, id);
            var result = statement.executeQuery();

            Room room = null;

            if(result.next()){
                room = buildRoom(result);
            }

            return Optional.ofNullable(room);
        }catch (SQLException e){
            throw new DaoException(e);
        }
    }

    private Room buildRoom(ResultSet result) throws SQLException {
        return Room.builder()
                .id(result.getLong("код"))
                .buildingId(result.getLong("код_корпуса"))
                .number(result.getString("номер"))
                .build();
    }

    @Override
    public boolean update(Room room) {
        try(var connection = ConnectionToMS.get();
            var statement = connection.prepareStatement(UPDATE_SQL)){
            statement.setLong(1, room.getBuildingId());
            statement.setString(2, room.getNumber());
            statement.setLong(3, room.getId());
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
    public List<Room> findAll() {
        try (var connection = ConnectionToMS.get();
             var statement = connection.prepareStatement(FIND_ALL_SQL)) {

            List<Room> rooms = new ArrayList<>();

            var resultSet = statement.executeQuery();

            while (resultSet.next()) {
                rooms.add(buildRoom(resultSet));
            }

            return rooms;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }
}
