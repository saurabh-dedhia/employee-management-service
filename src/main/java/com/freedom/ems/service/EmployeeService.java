package com.freedom.ems.service;

import com.freedom.ems.exception.DepartmentNotFoundException;
import com.freedom.ems.exception.EmployeeNotFoundException;
import com.freedom.ems.exception.IntegrityConstraintViolationException;
import com.freedom.ems.model.Department;
import com.freedom.ems.model.Employee;
import com.freedom.ems.repository.EmployeeRepository;
import com.freedom.ems.utils.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class EmployeeService {

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    DepartmentService departmentService;

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public List<Employee> getAllEmployeesByDepartment(int departmentId) {
        return employeeRepository.findByDepartment_Id(departmentId);
    }

    public Optional<Employee> getEmployee(int id) {
        return employeeRepository.findById(id);
    }

    public long getEmployeeCountByDepartment(int departmentId) {
        return employeeRepository.countByDepartment_Id(departmentId);
    }

    @Transactional
    public Employee createEmployee(int departmentId, Employee employee) {

        if (employeeRepository.existsByEmail(employee.getEmail())) {
            throw new IntegrityConstraintViolationException("Email already exists (should be unique)");
        }

        Department department =
                departmentService.getDepartment(departmentId).orElseThrow(DepartmentNotFoundException::new);
        employee.setDepartment(department);

        return employeeRepository.save(employee);
    }

    @Transactional
    public Employee updateEmployee(int id, Integer departmentId, Employee newEmployee, HttpMethod httpMethod) {

        Employee employee = getEmployee(id).orElseThrow(EmployeeNotFoundException::new);

        if (Objects.nonNull(departmentId)) {
            Department department =
                    departmentService.getDepartment(departmentId).orElseThrow(DepartmentNotFoundException::new);
            newEmployee.setDepartment(department);
        }

        if (Objects.nonNull(newEmployee.getEmail()) && !employee.getEmail()
                .equals(newEmployee.getEmail()) && employeeRepository
                .existsByEmail(newEmployee.getEmail())) {
            throw new IntegrityConstraintViolationException("Email already exists (should be unique)");
        }

        if (httpMethod.equals(HttpMethod.PATCH)) {

            Utility.copyEmployeeProperties(employee, newEmployee);
            return employeeRepository.save(employee);

        } else {
            newEmployee.setId(id);
            return employeeRepository.save(newEmployee);
        }
    }

    @Transactional
    public void deleteEmployee(int id) {
        employeeRepository.deleteById(id);
    }
}
