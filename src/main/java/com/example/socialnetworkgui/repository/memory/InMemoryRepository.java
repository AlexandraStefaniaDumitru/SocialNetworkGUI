package com.example.socialnetworkgui.repository.memory;


import com.example.socialnetworkgui.model.Entity;
import com.example.socialnetworkgui.model.exceptions.*;
import com.example.socialnetworkgui.model.validators.Validator;
import com.example.socialnetworkgui.repository.Repository;

import java.util.HashMap;
import java.util.Map;

public class InMemoryRepository<ID, E extends Entity<ID>> implements Repository<ID, E> {

    private final Validator<E> validator;
    Map<ID, E> entities;

    public InMemoryRepository(Validator<E> validator) {
        this.validator = validator;
        entities = new HashMap<>();
    }

    @Override
    public E findOne(ID id) {
        if (id == null)
            throw new IllegalArgumentException("id must be not null");
        return entities.get(id);
    }

    @Override
    public Iterable<E> findAll() {
        return entities.values();
    }

    @Override
    public E save(E entity) throws FriendshipExceptionAlreadyFriends, FriendshipExceptionNonexistentUser, UserExceptionEmptyID, FriendshipExceptionSameUser, UserExceptionEmptyFirstName, UserExceptionEmptyLastName {
        if (entity == null)
            throw new IllegalArgumentException("entity must be not null");
        validator.validate(entity);
        if (entities.get(entity.getId()) != null) {
            return entity;
        } else entities.put(entity.getId(), entity);
        return null;
    }

    @Override
    public ID remove(ID id) {
        if (id == null) {
            throw new IllegalArgumentException("entity must be not null");
        }
        return entities.remove(id).getId();
    }

    @Override
    public E update(E entity, E newEntity) throws RuntimeException, FriendshipExceptionAlreadyFriends, FriendshipExceptionNonexistentUser, UserExceptionEmptyID, FriendshipExceptionSameUser, UserExceptionEmptyFirstName, UserExceptionEmptyLastName {

        if (entity == null)
            throw new IllegalArgumentException("entity must be not null!");
        newEntity.setId(entity.getId());
        validator.validate(newEntity);
        entities.put(entity.getId(), newEntity);

        if (entities.get(entity.getId()) != null) {
            entities.put(entity.getId(), newEntity);
            return null;
        }
        return newEntity;

    }

}