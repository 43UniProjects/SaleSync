package org.oop_project.DatabaseHandler.migrations;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;
import org.bson.Document;

import java.util.Date;

// @ChangeUnit ensures this class runs once and its execution is recorded.
@ChangeUnit(id = "AddingDefaultAdmin", order = "007", author = "SaleSync")
public class DatabaseChangeUnit_007 {

    // Mongock automatically injects the MongoDatabase instance.
    @Execution
    public void execute(MongoDatabase db) {

        System.out.println("Inserting Default Admin with credentials of Username \"admin\" and Password \"1234\"");

        Document defaultAdmin = new Document()
                .append("id", "ADMIN-01")
                .append("firstName", "default")
                .append("lastName", "admin")
                .append("dob", new Date())
                .append("startDate", new Date())
                .append("role", "ADMIN")
                .append("email", "admin@gamil.com")
                .append("phoneNumber", "0701234567")
                .append("username", "admin")
                .append("password", "1234");

        db.getCollection("Employee").insertOne(defaultAdmin);

    }

    // Optional: Define rollback logic to undo the migration if it fails or is rolled back
    @RollbackExecution
    public void rollback(MongoDatabase database) {
        System.out.println("Deleting Default Admin");
        Document filter = new Document("username", Filters.eq("admin"));
        database.getCollection("Employee").deleteMany(filter);
    }
}
