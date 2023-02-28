package com.example.socialnetworkgui.repository.file;


import com.example.socialnetworkgui.model.Entity;
import com.example.socialnetworkgui.model.exceptions.*;
import com.example.socialnetworkgui.model.validators.Validator;
import com.example.socialnetworkgui.repository.memory.InMemoryRepository;

import java.io.*;
import java.util.Arrays;
import java.util.List;


public abstract class AbstractFileRepository<ID, E extends Entity<ID>> extends InMemoryRepository<ID, E> {
    String fileName;

    public AbstractFileRepository(String fileName, Validator<E> validator) throws FriendshipExceptionAlreadyFriends, FriendshipExceptionNonexistentUser, UserExceptionEmptyID, FriendshipExceptionSameUser, UserExceptionEmptyFirstName, UserExceptionEmptyLastName {
        super(validator);
        this.fileName = fileName;
        loadData();
    }

    private void loadData() throws FriendshipExceptionAlreadyFriends, FriendshipExceptionNonexistentUser, UserExceptionEmptyID, FriendshipExceptionSameUser, UserExceptionEmptyFirstName, UserExceptionEmptyLastName {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                List<String> attr = Arrays.asList(line.split(";"));
                E e = extractEntity(attr);
                super.save(e);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected abstract String createEntityAsString(E entity);

    /**
     * extract entity  - template method design pattern
     * creates an entity of type E having a specified list of @code attributes
     *
     * @param attributes
     * @return an entity of type E
     */
    public abstract E extractEntity(List<String> attributes);

    @Override
    public E save(E entity) throws FriendshipExceptionAlreadyFriends, FriendshipExceptionNonexistentUser, UserExceptionEmptyID, FriendshipExceptionSameUser, UserExceptionEmptyFirstName, UserExceptionEmptyLastName {
        E e = super.save(entity);
        if (e == null) {
            writeToFile(entity);
        }
        return e;
    }

    public ID remove(ID id) {
        ID e = super.remove(id);
        if (e != null) {
            writeEntities();
            return e;
        }
        return null;
    }

    public E update(E entity, E newEntity) throws RuntimeException, FriendshipExceptionAlreadyFriends, FriendshipExceptionNonexistentUser, UserExceptionEmptyID, FriendshipExceptionSameUser, UserExceptionEmptyFirstName, UserExceptionEmptyLastName {
        E e = super.update(entity, newEntity);
        writeEntities();
        return null;
    }

    protected void writeEntities() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
            for (E entity : findAll()) {
                bw.write(createEntityAsString(entity));
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    protected void writeToFile(E entity) {
        try (BufferedWriter bW = new BufferedWriter(new FileWriter(fileName, true))) {
            bW.write(createEntityAsString(entity));
            bW.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}