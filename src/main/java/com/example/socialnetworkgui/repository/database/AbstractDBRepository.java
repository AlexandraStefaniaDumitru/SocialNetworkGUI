package com.example.socialnetworkgui.repository.database;


import com.example.socialnetworkgui.model.Entity;
import com.example.socialnetworkgui.repository.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public abstract class AbstractDBRepository<ID, E extends Entity<ID>> implements Repository<ID, E> {

    private final JDBCUtils jdbcUtils = new JDBCUtils();

    protected abstract PreparedStatement getSaveStatement(Connection connection, E entity) throws SQLException;

    protected abstract PreparedStatement getRemoveStatement(Connection connection, ID id) throws SQLException;

    protected abstract PreparedStatement getUpdateStatement(Connection connection, E entity, E newEntity) throws SQLException;

    protected abstract String getTable();

    protected abstract E createEntity(ResultSet resultSet) throws SQLException;

    @Override
    public E findOne(ID id) {
        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * from " + getTable() + " WHERE id = ?")) {
            statement.setLong(1, (Long) id);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return createEntity(resultSet);
        } catch (SQLException e) {
            return null;
        }
    }

    @Override
    public Iterable<E> findAll() {
        Set<E> entities = new HashSet<>();
        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * from " + getTable());
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                E entity = createEntity(resultSet);
                entities.add(entity);
            }
            return entities;
        } catch (SQLException e) {
            return null;
        }
    }

    @Override
    public E save(E entity) {
        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement statement = getSaveStatement(connection, entity)) {
            statement.executeUpdate();
            return null;
        } catch (SQLException e) {
            return null;
        }
    }

    @Override
    public ID remove(ID id) {
        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement statement = getRemoveStatement(connection, id)) {
            statement.executeUpdate();
            return id;
        } catch (SQLException e) {
            return null;
        }
    }

    @Override
    public E update(E entity, E newEntity) {
        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement statement = getUpdateStatement(connection, entity, newEntity)) {
            statement.executeUpdate();
            return null;
        } catch (SQLException e) {
            return null;
        }
    }
}