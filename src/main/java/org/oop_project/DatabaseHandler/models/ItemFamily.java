package org.oop_project.DatabaseHandler.models;

import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.List;

public class ItemFamily {

    @BsonId
    private BsonId _id;
    @BsonProperty("id")
    private String id;
    private String label;
    private List<ItemSubFamily> subFamilies;

    public ItemFamily(String id, String label, List<ItemSubFamily> itemSubFamilies) {
        this.id = id;
        this.label = label;
        this.subFamilies = itemSubFamilies;
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

    public List<ItemSubFamily> getSubFamilies() {
        return subFamilies;
    }

    public void setSubFamilies(List<ItemSubFamily> subFamilies) {
        this.subFamilies = subFamilies;
    }

    public ItemSubFamily getSubFamily(String id) {
        for(ItemSubFamily itemSubFamily : this.subFamilies) {
            if(itemSubFamily.getId().equals(id)) {
                return itemSubFamily;
            }
        }
        return null;
    }
}
