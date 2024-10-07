package org.example.dao;

import org.example.connection.ConnectionToMS;
import org.example.entity.JobTitle;
import org.example.entity.SportSection;
import org.example.exception.DaoException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SportSectionDao implements Dao<Long, SportSection> {
    private static final SportSectionDao INSTANCE = new SportSectionDao();
    public static SportSectionDao getInstance(){return INSTANCE;}
    private SportSectionDao(){}

    private static final String SAVE_SQL = """
            insert into Спортивная_секция
            (название) 
            values (?)
            """;

    private static final String DELETE_SQL = """
            DELETE FROM Спортивная_секция 
            where код = ?
            """;

    private static final String FIND_ALL_SQL = """
            SELECT код, название
            FROM Спортивная_секция
            """;

    private static final String FIND_BY_ID_SQL = """
            SELECT код, название
            FROM Спортивная_секция
            where код = ?
            """;

    private static final String FIND_ID_BY_NAME_SQL = """
            select код from Спортивная_секция where название = ?
            """;

    private static final String UPDATE_SQL = """
            UPDATE Спортивная_секция
            SET название = ?
            where код = ?
            """;
    @Override
    public SportSection save(SportSection sportSection) {
        try(var connection = ConnectionToMS.get();
            var statement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS))
        {
            statement.setString(1, sportSection.getName());

            statement.executeUpdate();
            var keys = statement.getGeneratedKeys();
            if(keys.next()){
                sportSection.setId(keys.getLong(1));
            }

            return sportSection;
        }catch (SQLException e){
            throw new DaoException(e);
        }
    }

    public Optional<Long> findIdByName(String sportSectionName) {
        try(var connection = ConnectionToMS.get();
            var statement = connection.prepareStatement(FIND_ID_BY_NAME_SQL)){
            statement.setString(1, sportSectionName);
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
    public Optional<SportSection> findById(Long id) {
        try(var connection = ConnectionToMS.get();
            var statement = connection.prepareStatement(FIND_BY_ID_SQL)){
            statement.setLong(1, id);
            var result = statement.executeQuery();

            SportSection sportSection = null;

            if(result.next()){
                sportSection = buildSportSection(result);
            }

            return Optional.ofNullable(sportSection);
        }catch (SQLException e){
            throw new DaoException(e);
        }
    }

    @Override
    public boolean update(SportSection sportSection) {
        try(var connection = ConnectionToMS.get();
            var statement = connection.prepareStatement(UPDATE_SQL)){
            statement.setString(1, sportSection.getName());
            statement.setLong(2, sportSection.getId());
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
    public List<SportSection> findAll() {
        try(var connection = ConnectionToMS.get();
            var statement = connection.prepareStatement(FIND_ALL_SQL)){

            List<SportSection> sportSections = new ArrayList<>();

            var resultSet = statement.executeQuery();

            while (resultSet.next()){
                sportSections.add(buildSportSection(resultSet));
            }

            return sportSections;
        } catch (SQLException e){
            throw new DaoException(e);
        }
    }

    private SportSection buildSportSection(ResultSet resultSet) throws SQLException {
        return SportSection.builder()
                .id(resultSet.getLong("код"))
                .name(resultSet.getString("название"))
                .build();
    }

}
