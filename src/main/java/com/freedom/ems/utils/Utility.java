package com.freedom.ems.utils;

import com.freedom.ems.model.Employee;

import java.util.Objects;

public class Utility {

    public static void copyEmployeeProperties(Employee source, Employee target) {
        if (Objects.nonNull(source) && Objects.nonNull(target)) {
            if (Objects.nonNull(target.getFirstName())) source.setFirstName(target.getFirstName());
            if (Objects.nonNull(target.getLastName())) source.setLastName(target.getLastName());
            if (target.getSalary() > 0) source.setSalary(target.getSalary());
            if (Objects.nonNull(target.getEmail())) source.setEmail(target.getEmail());
            if (Objects.nonNull(target.getDepartment())) source.setDepartment(target.getDepartment());
        }
    }

}
