package org.oop_project.DatabaseHandler;

import org.bson.codecs.configuration.CodecProvider;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import static org.bson.codecs.pojo.PojoCodecProvider.builder;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

//import models.Employee;

public class DatabaseConnectionManager {
    private static final String CONNECTION_STRING = "mongodb://localhost:27017";
    private static final String DATABASE_NAME = "SaleSync";

    private MongoClient mongoClient;
    private MongoDatabase database;

    // private MongoCollection<Employee> userCollection;

    // Keep a single shared instance (simple singleton pattern)
    private static DatabaseConnectionManager INSTANCE;

    public DatabaseConnectionManager() {
        try {

            CodecProvider pojoCodecProvider = builder().automatic(true).build();

            // Combines default codec registry with pojo codec registry
            CodecRegistry pojoCodecRegistry = fromRegistries(
                    MongoClientSettings.getDefaultCodecRegistry(),
                    fromProviders(pojoCodecProvider)
            );

            ConnectionString connStr = new ConnectionString(CONNECTION_STRING);
            // Building mongo client settings builder obj
            MongoClientSettings.Builder mongoClientSettingsBuilder = MongoClientSettings.builder();
            // Adding connection string
            mongoClientSettingsBuilder.applyConnectionString(connStr);
            // Overriding default BSON codec registry with  POJO Codec registry
            mongoClientSettingsBuilder.codecRegistry(pojoCodecRegistry);
            // Building mongo client settings obj
            MongoClientSettings settings = mongoClientSettingsBuilder.build();

            mongoClient = MongoClients.create(settings);

            database = mongoClient.getDatabase(DATABASE_NAME);

            System.out.println(database);

            System.out.println("Connected to MongoDB successfully!");
        } catch (Exception e) {
            System.err.println("Error connecting to MongoDB: " + e.getMessage());
        }
    }


    public <T> MongoCollection<T> getCollection(String collectionName, Class<T> clazz) {
        return database.getCollection(collectionName, clazz);
    }

    /**
     * Obtain the shared instance of MongoConnectionManager.
     */
    public static synchronized DatabaseConnectionManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DatabaseConnectionManager();
        }
        return INSTANCE;
    }

    // --- Disconnect ---
    public void close() {
        if (mongoClient != null) {
            mongoClient.close();
            System.out.println("MongoDB connection closed.");
        }
    }
}
