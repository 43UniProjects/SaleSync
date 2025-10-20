package org.oop_project.DatabaseHandler.operations;

import org.oop_project.DatabaseHandler.DatabaseConnectionManager;

import java.util.HashMap;
import java.util.List;

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
    void delete(String identifier);
    default void closeConnection() {
        dbClient.close();
    }
}
