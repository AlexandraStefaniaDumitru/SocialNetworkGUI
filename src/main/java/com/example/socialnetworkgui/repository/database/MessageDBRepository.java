package com.example.socialnetworkgui.repository.database;

import com.example.socialnetworkgui.model.Message;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;

public class MessageDBRepository extends AbstractDBRepository<Long, Message> {
    @Override
    protected PreparedStatement getSaveStatement(Connection connection, Message entity) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO messages (id, sender, receiver, message, date_of, time_of) values (?,?,?,?,?,?)");
        statement.setInt(1, Math.toIntExact(entity.getId()));
        statement.setInt(2, Math.toIntExact(entity.getSender()));
        statement.setInt(3, Math.toIntExact(entity.getReceiver()));
        statement.setString(4, String.valueOf(entity.getMessage()));
        statement.setDate(5, Date.valueOf(entity.getDate_of()));
        statement.setTime(6,Time.valueOf(entity.getTime_of()));
        return statement;
    }

    @Override
    protected PreparedStatement getRemoveStatement(Connection connection, Long id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("DELETE FROM messages WHERE id = ?");
        statement.setInt(1, Math.toIntExact(id));
        return statement;
    }

    @Override
    protected PreparedStatement getUpdateStatement(Connection connection, Message entity, Message newEntity) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("UPDATE message SET message=?, date_of=?, time_of=? WHERE id=?");
        preparedStatement.setString(1, String.valueOf(newEntity.getMessage()));
        preparedStatement.setDate(2, Date.valueOf(newEntity.getDate_of()));
        preparedStatement.setTime(3,Time.valueOf(newEntity.getTime_of()));
        preparedStatement.setInt(4, Math.toIntExact(entity.getId()));
        return preparedStatement;
    }

    @Override
    protected String getTable() {
        return "messages";
    }

    @Override
    protected Message createEntity(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("id");
        Long idUser1 = resultSet.getLong("sender");
        Long idUser2 = resultSet.getLong("receiver");
        String message = resultSet.getString("message");
        LocalDate date_of = resultSet.getDate("date_of").toLocalDate();
        LocalTime time_of = resultSet.getTime("time_of").toLocalTime();
        Message messageEntity = new Message(idUser1, idUser2, message, date_of, time_of);
        messageEntity.setId(id);
        return messageEntity;
    }
}