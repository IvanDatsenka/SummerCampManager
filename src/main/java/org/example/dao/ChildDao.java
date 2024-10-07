package org.example.dao;

import org.example.connection.ConnectionToMS;
import org.example.entity.Child;
import org.example.exception.DaoException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ChildDao implements Dao<Long, Child>{
    private final static ChildDao INSTANCE = new ChildDao();

    private static final String SAVE_SQL = """
            insert into Ребёнок
            (имя, фамилия, код_отряда, код_спотивной_секции, код_комнаты) 
            values (?,?,?,?,?)
            """;

    private static final String DELETE_SQL = """
            DELETE FROM Ребёнок 
            where код = ?
            """;

    private static final String FIND_ALL_SQL = """
            SELECT код, имя, фамилия, код_отряда, код_спотивной_секции, код_комнаты 
            FROM Ребёнок
            """;

    private static final String FIND_BY_ID_SQL = """
            SELECT код, имя, фамилия, код_отряда, код_спотивной_секции, код_комнаты 
            FROM Ребёнок
            where код = ?
            """;

    private static final String UPDATE_SQL = """
            UPDATE Ребёнок
            SET имя = ?, фамилия = ?, код_отряда = ?, код_спотивной_секции = ?, код_комнаты = ?
            where код = ?
            """;

    public Child save(Child child){
        try(var connection = ConnectionToMS.get();
            var statement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS))
        {
            statement.setString(1, child.getFirstName());
            statement.setString(2, child.getSecondName());
            statement.setLong(3, child.getSquadId());
            statement.setLong(4, child.getSportSectionId());
            statement.setLong(5, child.getRoomId());

            statement.executeUpdate();
            var keys = statement.getGeneratedKeys();
            if(keys.next()){
                child.setId(keys.getLong(1));
            }

            return child;
        }catch (SQLException e){
            throw new DaoException(e);
        }
    }


    public Optional<Child> findById(Long id){
        try(var connection = ConnectionToMS.get();
        var statement = connection.prepareStatement(FIND_BY_ID_SQL)){
            statement.setLong(1, id);
            var result = statement.executeQuery();

            Child child = null;

            if(result.next()){
                child = buildChild(result);
            }

            return Optional.ofNullable(child);
        }catch (SQLException e){
            throw new DaoException(e);
        }
    }

    public boolean update (Child child){
        try(var connection = ConnectionToMS.get();
            var statement = connection.prepareStatement(UPDATE_SQL)){
            statement.setString(1, child.getFirstName());
            statement.setString(2, child.getSecondName());
            statement.setLong(3, child.getSquadId());
            statement.setLong(4, child.getSportSectionId());
            statement.setLong(5, child.getRoomId());
            statement.setLong(6, child.getId());

            return statement.executeUpdate() > 0;
        } catch (SQLException e){
            throw new DaoException(e);
        }
    }

    private static Child buildChild(ResultSet result) throws SQLException {
        return Child.builder()
                .id(result.getLong("код"))
                .firstName(result.getString("имя"))
                .secondName(result.getString("фамилия"))
                .squadId(result.getLong("код_отряда"))
                .sportSectionId(result.getLong("код_спотивной_секции"))
                .roomId(result.getLong("код_комнаты"))
                .build();
    }

    public boolean delete(Long id){
        try(var connection = ConnectionToMS.get();
        var statement = connection.prepareStatement(DELETE_SQL)){
            statement.setLong(1, id);

            return statement.executeUpdate() > 0;
        } catch (SQLException e){
            throw new DaoException(e);
        }
    }

    public List<Child> findAll(){
        try(var connection = ConnectionToMS.get();
            var statement = connection.prepareStatement(FIND_ALL_SQL)){

            List<Child> children = new ArrayList<>();

            var resultSet = statement.executeQuery();

            while (resultSet.next()){
                children.add(buildChild(resultSet));
            }

            return children;
        } catch (SQLException e){
            throw new DaoException(e);
        }
    }
    public static ChildDao getInstance(){
        return INSTANCE;
    }

    private ChildDao(){}
}
