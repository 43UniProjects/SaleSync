package org.oop_project.DatabaseHandler.models;

import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.Date;

public class Supplier {

    private BsonId _id;
    @BsonProperty("id")
    private String id;
    private String name;
    private String phoneNumber;
    private String email;
    private Date joinedDate;
    private boolean isActive;

    public Supplier(String id, String name, String phoneNumber, String email, Date joinedDate, boolean isActive) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.joinedDate = joinedDate;
        this.isActive = isActive;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getJoinedDate() {
        return joinedDate;
    }

    public void setJoinedDate() {
        this.joinedDate = new Date();
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        this.isActive = active;
    }
}
