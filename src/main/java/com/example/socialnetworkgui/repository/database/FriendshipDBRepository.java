package com.example.socialnetworkgui.repository.database;


import com.example.socialnetworkgui.model.Friendship;
import com.example.socialnetworkgui.model.FriendshipStatus;

import java.sql.*;
import java.time.LocalDate;

public class FriendshipDBRepository extends AbstractDBRepository<Long, Friendship> {

    @Override
    protected PreparedStatement getSaveStatement(Connection connection, Friendship entity) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO friendships (id, sender, receiver, friends_from, friendship_status) values (?,?,?,?,?)");
        statement.setInt(1, Math.toIntExact(entity.getId()));
        statement.setInt(2, Math.toIntExact(entity.getSender()));
        statement.setInt(3, Math.toIntExact(entity.getReceiver()));
        statement.setDate(4, Date.valueOf(entity.getFriendsFrom()));
        statement.setString(5, String.valueOf(entity.getFriendshipStatus()));
        return statement;
    }

    @Override
    protected PreparedStatement getRemoveStatement(Connection connection, Long id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("DELETE FROM friendships WHERE id = ?");
        statement.setInt(1, Math.toIntExact(id));
        return statement;
    }

    @Override
    protected PreparedStatement getUpdateStatement(Connection connection, Friendship entity, Friendship newEntity) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("UPDATE friendships SET friends_from=?, friendship_status=? WHERE id=?");
        preparedStatement.setDate(1, Date.valueOf(newEntity.getFriendsFrom()));
        preparedStatement.setString(2, String.valueOf(newEntity.getFriendshipStatus()));
        preparedStatement.setInt(3, Math.toIntExact(entity.getId()));
        return preparedStatement;
    }

    @Override
    protected String getTable() {
        return "friendships";
    }

    @Override
    protected Friendship createEntity(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("id");
        Long idUser1 = resultSet.getLong("sender");
        Long idUser2 = resultSet.getLong("receiver");
        LocalDate friendsFrom = resultSet.getDate("friends_from").toLocalDate();
        FriendshipStatus friendshipStatus = FriendshipStatus.valueOf(resultSet.getString("friendship_status"));
        Friendship friendship = new Friendship(idUser1, idUser2, friendsFrom, friendshipStatus);
        friendship.setId(id);
        return friendship;
    }
}