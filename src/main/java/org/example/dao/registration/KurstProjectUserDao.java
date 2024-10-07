package org.example.dao.registration;


import org.example.connection.ConnectionToMS;
import org.example.dao.Dao;
import org.example.entity.Building;
import org.example.entity.PrUser;
import org.example.exception.DaoException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class KurstProjectUserDao implements Dao<Long, PrUser> {
    private final static String SELECT_USERS = "select * from kursProjectUser";
    private final static String DELETE_USERS = "delete from kursProjectUser where id = ?";

    private final static String UPDATE_ROLE_SQL = """
            UPDATE kursProjectUser
            SET userRole = ?
            where id = ?
            """;
    private final static String INSERT_USER = """
            insert into kursProjectUser
            (userPassword, userName, userRole)
            values (?,?,?)
            """;
    private final static String UPDATE_USER_ROLE = """
            UPDATE kursProjectUser
            SET userRole = ?
            where код = ?
            """;

    @Override
    public PrUser save(PrUser prUser) {
        try(var connection = ConnectionToMS.get();
            var statement = connection.prepareStatement(INSERT_USER, Statement.RETURN_GENERATED_KEYS))
        {
            statement.setString(1, prUser.getPassword());
            statement.setString(2, prUser.getUserName());
            statement.setString(3, prUser.getRole());

            statement.executeUpdate();
            var keys = statement.getGeneratedKeys();
            if(keys.next()){
                prUser.setId(keys.getLong(1));
            }

            return prUser;
        }catch (SQLException e){
            throw new DaoException(e);
        }
    }

    @Override
    public Optional<PrUser> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public boolean update(PrUser prUser) {
        String currentRole;
        if(prUser.getRole().equals("default")){
            currentRole = "admin";
        }else {
            currentRole = "default";
        }
        try(var connection = ConnectionToMS.get();
            var statement = connection.prepareStatement(UPDATE_ROLE_SQL)){
            statement.setString(1, currentRole);
            statement.setLong(2, prUser.getId());
            return statement.executeUpdate() > 0;
        } catch (SQLException e){
            throw new DaoException(e);
        }
    }

    @Override
    public boolean delete(Long id) {
        return false;
    }

    @Override
    public List<PrUser> findAll() {
        try(var connection = ConnectionToMS.get();
            var statement = connection.prepareStatement(SELECT_USERS)){

            List<PrUser> prUsers = new ArrayList<>();

            var resultSet = statement.executeQuery();

            while (resultSet.next()){
                prUsers.add(buildUsers(resultSet));
            }

            return prUsers;
        } catch (SQLException e){
            throw new DaoException(e);
        }
    }

    private PrUser buildUsers(ResultSet resultSet) throws SQLException {
        return PrUser.builder()
                .id(resultSet.getLong("id"))
                .userName(resultSet.getString("userName"))
                .password(resultSet.getString("userPassword"))
                .role(resultSet.getString("userRole"))
                .build();
    }
}
