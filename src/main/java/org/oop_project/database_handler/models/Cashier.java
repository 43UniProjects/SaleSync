package org.oop_project.database_handler.models;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.oop_project.database_handler.enums.Role;


class Cashier extends Employee {
    private BigDecimal salary = BigDecimal.valueOf(35_000.00);
    private BigDecimal otRate = BigDecimal.valueOf(0.15);
    private int dayOffsPerMonth = 15;

    public Cashier(String userId, String firstName, String lastName, LocalDate dob, String phoneNumber, String email, String username, Role role, LocalDate startDate) {
        super(userId, firstName, lastName, dob, phoneNumber, email, username, role, startDate);

    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public BigDecimal getOtRate() {
        return otRate;
    }

    public void setOtRate(BigDecimal otRate) {
        this.otRate = otRate;
    }

    public int getDayOffsPerMonth() {
        return dayOffsPerMonth;
    }

    public void setDayOffsPerMonth(int dayOffsPerMonth) {
        this.dayOffsPerMonth = dayOffsPerMonth;
    }


}
