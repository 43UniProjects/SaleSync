package org.oop_project.utils;

import org.oop_project.database_handler.enums.Role;
import org.oop_project.database_handler.models.Employee;
import org.oop_project.database_handler.models.Product;
import org.oop_project.database_handler.operations.Operations;

public class Generate {


    /*
    * Ex: ADMIN-01 > (FORMAT Role AssignedId)
    * */
    public static String generateUserId(Operations<Employee> employeeOps, Role role) {
        String lastId = employeeOps.getLastId();
        //System.out.println(lastId);

        if (lastId == null) {
            return String.format("%s-01", role.getLabel());
        }

        int idNumber = Integer.parseInt(lastId.split("-")[1]) + 1;
        return String.format("%s-%s", role.getLabel(), idNumber < 10 ? "0" + idNumber : idNumber);
    }

    /*
     * Ex: ELC-WIRE-01 > (FORMAT ItemFamily-ItemSubFamily-AssignedId)
     * */
    public static String generateProductId(Operations<Product> productOps, String itemFamily, String itemSubFamily) {
        String lastId = productOps.getLastId();

        if (lastId == null) {
            return String.format("%s-%s-01", itemFamily, itemSubFamily);
        }

        int idNumber = Integer.parseInt(lastId.split("-")[2]) + 1;
        return itemFamily + "-" + itemSubFamily + "-" + (idNumber < 10 ? "0" + String.valueOf(idNumber) : String.valueOf(idNumber));
    }


}