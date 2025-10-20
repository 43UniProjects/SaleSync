package org.oop_project.utils;


import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


public class JsonReader {

    public static JSONObject read(String path){
        String jsonString = "";
        JSONObject jsonObject = null;
        try {
            jsonString = Files.readString(Paths.get(path));
            jsonObject = new JSONObject(jsonString);
        } catch (IOException e) {
            e.printStackTrace();
            // Handle file reading error
        }
        return jsonObject;
    }

}