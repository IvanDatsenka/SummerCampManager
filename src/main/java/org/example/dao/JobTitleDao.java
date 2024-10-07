package org.example.dao;

import org.example.connection.ConnectionToMS;
import org.example.entity.Child;
import org.example.entity.JobTitle;
import org.example.exception.DaoException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class JobTitleDao implements Dao<Long, JobTitle>{

    private final static JobTitleDao INSTANCE = new JobTitleDao();

    private static final String SAVE_SQL = """
            insert into Должность
            (название) 
            values (?)
            """;

    private static final String DELETE_SQL = """
            DELETE FROM Должность 
            where код = ?
            """;

    private static final String FIND_ALL_SQL = """
            SELECT код, название
            FROM Должность
            """;

    private static final String FIND_BY_ID_SQL = """
            SELECT код, название
            FROM Должность
            where код = ?
            """;

    private static final String UPDATE_SQL = """
            UPDATE Должность
            SET название = ?
            where код = ?
            """;

    private static final String FIND_ID_AND_NAMES_SQL = """
           select код, название
           from Должность
           """;
    @Override
    public JobTitle save(JobTitle jobTitle) {
        try(var connection = ConnectionToMS.get();
            var statement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS))
        {
            statement.setString(1, jobTitle.getName());

            statement.executeUpdate();
            var keys = statement.getGeneratedKeys();
            if(keys.next()){
                jobTitle.setId(keys.getLong(1));
            }

            return jobTitle;
        }catch (SQLException e){
            throw new DaoException(e);
        }
    }

    @Override
    public Optional<JobTitle> findById(Long id) {
        try(var connection = ConnectionToMS.get();
            var statement = connection.prepareStatement(FIND_BY_ID_SQL)){
            statement.setLong(1, id);
            var result = statement.executeQuery();

            JobTitle jobTitle = null;

            if(result.next()){
                jobTitle = buildJobTitle(result);
            }

            return Optional.ofNullable(jobTitle);
        }catch (SQLException e){
            throw new DaoException(e);
        }
    }

    @Override
    public boolean update(JobTitle jobTitle) {
        try(var connection = ConnectionToMS.get();
            var statement = connection.prepareStatement(UPDATE_SQL)){
            statement.setString(1, jobTitle.getName());
            statement.setLong(2, jobTitle.getId());
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
    public List<JobTitle> findAll() {
        try(var connection = ConnectionToMS.get();
            var statement = connection.prepareStatement(FIND_ALL_SQL)){

            List<JobTitle> jobTitles = new ArrayList<>();

            var resultSet = statement.executeQuery();

            while (resultSet.next()){
                jobTitles.add(buildJobTitle(resultSet));
            }

            return jobTitles;
        } catch (SQLException e){
            throw new DaoException(e);
        }
    }

    private JobTitle buildJobTitle(ResultSet resultSet) throws SQLException {
        return JobTitle.builder()
                .id(resultSet.getLong("код"))
                .name(resultSet.getString("название"))
                .build();
    }

    private JobTitleDao(){}
    public static JobTitleDao getInstance(){
        return INSTANCE;
    }

    public HashMap<Long, String> getIdAndNames() {
        HashMap<Long, String> idNames = new HashMap<>();
        try(var connection = ConnectionToMS.get();
            var statement = connection.prepareStatement(FIND_ID_AND_NAMES_SQL)) {
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
