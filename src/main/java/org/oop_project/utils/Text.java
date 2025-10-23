package org.oop_project.utils;


public class Text {

    public static String getFirstLetter(String word) {
        return word.substring(0, 1);
    }

    public static String capitalize(String word) {
        return word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();
    }

    public static String textCapitalize(String text) {

        StringBuilder returnStr = new StringBuilder();

        String sep = " ";
        String[] words = text.split(sep);
        int wordCount = words.length;

        for (String word : words) {
            word = getFirstLetter(word).toUpperCase() + word.substring(1).toLowerCase();
            if (wordCount-- != 1) {
                word += sep;
            }
            returnStr.append(word);
        }
        return returnStr.toString();
    }
}
