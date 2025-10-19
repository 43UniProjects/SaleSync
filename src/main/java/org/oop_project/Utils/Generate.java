package org.oop_project.Utils;

import org.oop_project.DatabaseHandler.Models.ItemFamily;
import org.oop_project.DatabaseHandler.Enums.Role;
import org.oop_project.DatabaseHandler.Models.ItemSubFamily;
import org.oop_project.DatabaseHandler.Models.Product;
import org.oop_project.DatabaseHandler.Operations.EmployeeOperations;
import org.oop_project.DatabaseHandler.Operations.ProductOperations;

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

    public static String generateUserId(EmployeeOperations employeeOps, Role role) {

        String lastId = employeeOps.getLastId();

        if(lastId == null) {
            return  role.getLabel() + "-" + "01";
        }

        int idNumber = Integer.parseInt(lastId.split("-")[1]) + 1;
        return  role.getLabel() + "-" + (idNumber < 10 ? "0" + idNumber : idNumber);
    }

    public static String generateProductId(ProductOperations productOps, ItemFamily itemFamily, ItemSubFamily itemSubFamily) {

        String lastId = productOps.getLastId();

        if(lastId == null) {
            return  itemFamily.getId() + itemSubFamily.getId() + "01";
        }

        int idNumber = Integer.parseInt(lastId.split("-")[2]) + 1;
        return  itemFamily.getId() + itemSubFamily.getId() + "-" + (idNumber < 10 ? "0" + idNumber : idNumber);
    }


}