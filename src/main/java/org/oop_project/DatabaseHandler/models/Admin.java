package org.oop_project.DatabaseHandler.models;

import org.oop_project.DatabaseHandler.enums.Role;

import java.math.BigDecimal;
import java.time.LocalDate;

class Admin extends Employee {

    private BigDecimal salary = BigDecimal.valueOf(75_000.00);
    private BigDecimal otRate = BigDecimal.valueOf(0.35);
    private int dayOffsPerMonth = 10;

    public Admin(String userId, String firstName, String lastName, LocalDate dob, String phoneNumber, String email, String username, Role role, LocalDate startDate) {
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
