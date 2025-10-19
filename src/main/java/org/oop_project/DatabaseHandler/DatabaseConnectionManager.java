package org.oop_project.DatabaseHandler;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import io.mongock.driver.mongodb.sync.v4.driver.MongoSync4Driver;
import io.mongock.runner.standalone.MongockStandalone;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;
import static org.bson.codecs.pojo.PojoCodecProvider.builder;

public class DatabaseConnectionManager {

    private static final String CONNECTION_STRING = "mongodb://localhost:27017/?retryWrites=false";
    private static final String MIGRATION_PACKAGE = "org.oop_project.DatabaseHandler.migrations";
    private static final String DATABASE_NAME = "SaleSync";
    public static final String EMPLOYEE_COLLECTION_NAME = "Employee";
    public static final String PRODUCT_COLLECTION_NAME = "Product";
    public static final String SUPPLIER_COLLECTION_NAME = "Supplier";
    public static final String ITEM_FAMILY_COLLECTION_NAME = "ItemFamily";


    private static DatabaseConnectionManager INSTANCE; // Keep a single shared instance (simple singleton pattern)
    private MongoClient mongoClient;
    private MongoDatabase database;

    private int i = 0;

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
            // Overriding default BSON codec registry with POJO Codec registry
            mongoClientSettingsBuilder.codecRegistry(pojoCodecRegistry);
            // Building mongo client settings obj
            MongoClientSettings settings = mongoClientSettingsBuilder.build();

            mongoClient = MongoClients.create(settings);

            this.database = mongoClient.getDatabase(DATABASE_NAME);

            System.out.println("Connected to MongoDB successfully!" + ++i);

            System.out.println("Application starting. Initializing Mongock migration...");

            // 2. Build the Mongock Driver
            MongoSync4Driver driver = MongoSync4Driver.withDefaultLock(mongoClient, DATABASE_NAME);
            driver.disableTransaction();

            // 3. Build and execute the Mongock Runner
            MongockStandalone.builder()
                    .setDriver(driver)
                    .addMigrationScanPackage(MIGRATION_PACKAGE)
                    .buildRunner()
                    .execute();

            System.out.println("✅ Mongock migration completed successfully.");

            // --- Application continues here after database is migrated ---
            System.out.println("Application is now fully initialized and starting up...");

        } catch (Exception e) {
            System.err.println("❌ An error occurred during Mongock execution. Application failed to start.");
            e.printStackTrace();
        }
    }

    public static synchronized DatabaseConnectionManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DatabaseConnectionManager();
        }
        return INSTANCE;
    }

    public <T> MongoCollection<T> getCollection(String collectionName, Class<T> clazz) {
        return this.database.getCollection(collectionName, clazz);
    }

    // --- Disconnect ---

    public void close() {
        if (mongoClient != null) {
            mongoClient.close();
            System.out.println("MongoDB connection closed.");
        }

    }

}