package org.oop_project.database_handler.operations;

import java.util.HashMap;
import java.util.List;

import org.oop_project.database_handler.DatabaseConnectionManager;

/*
* Base class for the collection operations in the database
* */

public interface Operations<T> {

    DatabaseConnectionManager dbClient = DatabaseConnectionManager.getInstance();

    void add(T entity);
    boolean find(String identifier);
    T get(String identifier);
    List<T> getAll();
    String getLastId();
    void update(String id, HashMap<String, Object> updatedEntries);
    boolean delete(String identifier);
    default void closeConnection() {
        dbClient.close();
    }
}
