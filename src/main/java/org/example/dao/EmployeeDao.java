package org.example.dao;

import org.example.connection.ConnectionToMS;
import org.example.entity.Child;
import org.example.entity.Employee;
import org.example.exception.DaoException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class EmployeeDao implements Dao<Long, Employee> {

    private static final EmployeeDao INSTANCE = new EmployeeDao();

    private EmployeeDao(){}

    private static final String SAVE_SQL = """
            insert into Сотрудник
            (имя, фамилия, код_должности)
            values (?,?,?)
            """;

    private static final String DELETE_SQL = """
            DELETE FROM Сотрудник 
            where код = ?
            """;

    private static final String FIND_ALL_SQL = """
            SELECT код, имя, фамилия, код_должности
            FROM Сотрудник
            """;

    private static final String FIND_BY_ID_SQL = """
            SELECT код, имя, фамилия, код_должности
            FROM Сотрудник
            where код = ?
            """;

    private static final String UPDATE_SQL = """
            UPDATE Сотрудник
            SET имя = ?, фамилия = ?, код_должности = ?
            where код = ?
            """;

    private static final String GET_ID_AND_NAMES_SQL = """
            select код, имя, фамилия
            from Сотрудник
            """;

    public static EmployeeDao getInstance(){return INSTANCE;}

    @Override
    public Employee save(Employee employee) {
        try(var connection = ConnectionToMS.get();
            var statement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS))
        {
            statement.setString(1, employee.getFirstName());
            statement.setString(2, employee.getSecondName());
            statement.setLong(3, employee.getJobTitleId());

            statement.executeUpdate();
            var keys = statement.getGeneratedKeys();
            if(keys.next()){
                employee.setId(keys.getLong(1));
            }

            return employee;
        }catch (SQLException e){
            throw new DaoException(e);
        }
    }


    @Override
    public Optional<Employee> findById(Long id) {
        try(var connection = ConnectionToMS.get();
            var statement = connection.prepareStatement(FIND_BY_ID_SQL)){
            statement.setLong(1, id);
            var result = statement.executeQuery();

            Employee employee = null;

            if(result.next()){
                employee = buildEmployee(result);
            }

            return Optional.ofNullable(employee);
        }catch (SQLException e){
            throw new DaoException(e);
        }
    }

    private Employee buildEmployee(ResultSet result) throws SQLException {
        return Employee.builder()
                .id(result.getLong("код"))
                .firstName(result.getString("имя"))
                .secondName(result.getString("фамилия"))
                .jobTitleId(result.getLong("код_должности"))
                .build();
    }

    @Override
    public boolean update(Employee employee) {
        try(var connection = ConnectionToMS.get();
            var statement = connection.prepareStatement(UPDATE_SQL)){
            statement.setString(1, employee.getFirstName());
            statement.setString(2, employee.getSecondName());
            statement.setLong(3, employee.getJobTitleId());
            statement.setLong(4, employee.getId());
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
    public List<Employee> findAll() {
        try(var connection = ConnectionToMS.get();
            var statement = connection.prepareStatement(FIND_ALL_SQL)){

            List<Employee> employees = new ArrayList<>();

            var resultSet = statement.executeQuery();

            while (resultSet.next()){
                employees.add(buildEmployee(resultSet));
            }

            return employees;
        } catch (SQLException e){
            throw new DaoException(e);
        }
    }

    public HashMap<Long, String> getIdAndNames() {
        HashMap<Long, String> idNames = new HashMap<>();
        try(var connection = ConnectionToMS.get();
            var statement = connection.prepareStatement(GET_ID_AND_NAMES_SQL)) {
            var result = statement.executeQuery();

            while(result.next()) {
                String name = result.getString("имя") + " " + result.getString("фамилия");
                idNames.put(result.getLong("код"), name);
            }
            return idNames;
        }catch (SQLException e){
            throw new DaoException(e);
        }
    }
}
