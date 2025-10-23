package org.oop_project.DatabaseHandler.models;

import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;

public class ItemSubFamily {
    @BsonId
    private BsonId _id;
    @BsonProperty("id")
    private String id;
    private String label;

    public ItemSubFamily(String id, String label) {
        this.id = id;
        this.label = label;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
