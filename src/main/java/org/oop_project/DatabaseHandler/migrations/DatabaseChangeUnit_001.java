package org.oop_project.DatabaseHandler.migrations;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.*;
import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;
import org.bson.Document;
import org.oop_project.DatabaseHandler.Enums.Role;

import java.util.Arrays;

import static org.oop_project.DatabaseHandler.DatabaseConnectionManager.EMPLOYEE_COLLECTION_NAME;

// @ChangeUnit ensures this class runs once and its execution is recorded.
@ChangeUnit(id = "EmployeeSetup", order = "001", author = "SaleSync")
public class DatabaseChangeUnit_001 {

    // Mongock automatically injects the MongoDatabase instance.
    @Execution
    public void execute(MongoDatabase db) {

        System.out.println("- Creating 'Employee' Collection...");

        Document employeeJsonSchema = buildEmployeeSchema();

        ValidationOptions employeeValidationOptions = new ValidationOptions()
                .validator(employeeJsonSchema)
                .validationLevel(ValidationLevel.STRICT)
                .validationAction(ValidationAction.ERROR);

        CreateCollectionOptions employeeCollectionOptions = new CreateCollectionOptions()
                .validationOptions(employeeValidationOptions);

        db.createCollection(EMPLOYEE_COLLECTION_NAME, employeeCollectionOptions);
        db.getCollection(EMPLOYEE_COLLECTION_NAME).createIndex(Indexes.ascending("id"), new IndexOptions().unique(true));
    }

    // Optional: Define rollback logic to undo the migration if it fails or is rolled back
    @RollbackExecution
    public void rollback(MongoDatabase database) {
        System.out.println("Rollback V001: Dropping collection 'Employee");
        database.getCollection(EMPLOYEE_COLLECTION_NAME).drop();
    }

    private Document buildEmployeeSchema() {
        return new Document("$jsonSchema", new Document()
                .append("required", Arrays.asList("id", "firstName", "lastName", "dob", "phoneNumber", "email", "username", "password", "startDate", "role"))
                .append("properties", new Document()
                        .append("id", new Document()
                                .append("bsonType", "string")
                        )
                        .append("firstName", new Document()
                                .append("bsonType", "string")
                                .append("description", "first name must be a string")
                        )
                        .append("lastName", new Document()
                                .append("bsonType", "string")
                                .append("description", "last name must be a string")
                        )
                        .append("dob", new Document()
                                .append("bsonType", "date")
                                .append("description", "birth date of the employee")
                        )
                        .append("phoneNumber", new Document()
                                .append("bsonType", "string")
                                .append("minLength", 10)
                        )
                        .append("email", new Document()
                                .append("bsonType", "string")
                                .append("pattern", "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
                                .append("description", "Invalid Email")
                        )
                        .append("username", new Document()
                                .append("bsonType", "string")
                                .append("minLength", 3)
                                .append("description", "username should've min length of 3")
                        )

                        .append("password", new Document()
                                .append("bsonType", "string")
                        )
                        .append("startDate", new Document()
                                .append("bsonType", "date")
                                .append("description", "date of admission of the employee")
                        )
                        .append("role", new Document()
                                .append("bsonType", "string")
                                .append("enum", Arrays.asList(
                                                Role.ADMIN.getLabel(),
                                                Role.CASHIER.getLabel(),
                                                Role.PRODUCT_MANAGER.getLabel()
                                        )
                                )

                        )
                )
        );

    }

}