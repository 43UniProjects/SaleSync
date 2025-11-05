package org.oop_project.utils;

import org.oop_project.DatabaseHandler.enums.Role;
import org.oop_project.DatabaseHandler.operations.EmployeeOperations;
import org.oop_project.DatabaseHandler.operations.ProductOperations;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class Generate {

    private static final Random random = new Random();
    private static final MessageDigest sha224;

    static {
        try {
            sha224 = MessageDigest.getInstance("SHA-224");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static String hashPassword(String password) {
        byte[] hashBytes = sha224.digest(password.getBytes());
        // Convert byte array to hex string
        StringBuilder hexStr = new StringBuilder();
        for (byte _byte : hashBytes) {
            String hex = Integer.toHexString(0xff & _byte);
            if (hex.length() == 1) {
                hexStr.append('0');
            }
            hexStr.append(hex);
        }
        return hexStr.toString();
    }

    /*
    * Ex: ADMIN-01 > (FORMAT Role AssignedId)
    * */
    public static String generateUserId(EmployeeOperations employeeOps, Role role) {
        String lastId = employeeOps.getLastId();
        System.out.println(lastId);

        if (lastId == null) {
            return String.format("%s-01", role.getLabel());
        }

        int idNumber = Integer.parseInt(lastId.split("-")[1]) + 1;
        return String.format("%s-%s", role.getLabel(), idNumber < 10 ? "0" + idNumber : idNumber);
    }

    /*
     * Ex: ELC-WIRE-01 > (FORMAT ItemFamily-ItemSubFamily-AssignedId)
     * */
    public static String generateProductId(ProductOperations productOps, String itemFamily, String itemSubFamily) {
        String lastId = productOps.getLastId();

        if (lastId == null) {
            return String.format("%s-%s-01", itemFamily, itemSubFamily);
        }

        int idNumber = Integer.parseInt(lastId.split("-")[2]) + 1;
        return itemFamily + "-" + itemSubFamily + "-" + (idNumber < 10 ? "0" + String.valueOf(idNumber) : String.valueOf(idNumber));
    }


}