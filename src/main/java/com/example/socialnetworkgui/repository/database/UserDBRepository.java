package com.example.socialnetworkgui.repository.database;

import com.example.socialnetworkgui.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDBRepository extends AbstractDBRepository<Long, User> {

    @Override
    protected PreparedStatement getSaveStatement(Connection connection, User entity) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO users (id, first_name, last_name) VALUES (?,?,?)");
        statement.setInt(1, Math.toIntExact(entity.getId()));
        statement.setString(2, entity.getFirstName());
        statement.setString(3, entity.getLastName());
        return statement;
    }

    @Override
    protected PreparedStatement getRemoveStatement(Connection connection, Long id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("DELETE FROM users WHERE id = ?");
        statement.setInt(1, Math.toIntExact(id));
        return statement;
    }

    @Override
    protected PreparedStatement getUpdateStatement(Connection connection, User entity, User newEntity) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("UPDATE users SET first_name = ?, last_name = ? WHERE id = ?");
        statement.setString(1, newEntity.getFirstName());
        statement.setString(2, newEntity.getLastName());
        statement.setInt(3, Math.toIntExact(entity.getId()));
        return statement;
    }

    @Override
    protected String getTable() {
        return "users";
    }

    @Override
    protected User createEntity(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("id");
        String firstName = resultSet.getString("first_name");
        String lastName = resultSet.getString("last_name");
        User user = new User(firstName, lastName);
        user.setId(id);
        return user;
    }
}