package org.oop_project.utils;

import java.util.List;

import org.oop_project.database_handler.enums.Role;
import org.oop_project.database_handler.models.Admin;
import org.oop_project.database_handler.models.Cashier;
import org.oop_project.database_handler.models.Employee;
import org.oop_project.database_handler.models.ProductManager;

public class EmployeeMapper {

    public static Employee mapEmployee(Employee e) {
        switch (e.getRole()) {
            case ADMIN: {
                Admin a = new Admin(e.getId(), e.getFirstName(), e.getLastName(), e.getDob(), e.getPhoneNumber(), e.getEmail(), e.getUsername(), Role.ADMIN, e.getStartDate());
                a.setPassword(e.getPassword());
                return a;
            }
            case CASHIER: {
                Cashier c = new Cashier(e.getId(), e.getFirstName(), e.getLastName(), e.getDob(), e.getPhoneNumber(), e.getEmail(), e.getUsername(), Role.CASHIER, e.getStartDate());
                c.setPassword(e.getPassword());
                return c;
            }
            case PRODUCT_MANAGER: {
                ProductManager p = new ProductManager(e.getId(), e.getFirstName(), e.getLastName(), e.getDob(), e.getPhoneNumber(), e.getEmail(), e.getUsername(), Role.PRODUCT_MANAGER, e.getStartDate());
                p.setPassword(e.getPassword());
                return p;
            }
        }

        return e;
    }

    public static List<Employee> mapEmployees(List<Employee> employeeList) {
        return employeeList.stream().map(EmployeeMapper::mapEmployee).toList();
    }
}
