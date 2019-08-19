package com.freedom.ems.service;

import com.freedom.ems.exception.EmployeeNotFoundException;
import com.freedom.ems.model.Department;
import com.freedom.ems.model.Employee;
import com.freedom.ems.repository.EmployeeRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpMethod;

import java.util.Optional;

public class EmployeeServiceTest {

    @InjectMocks
    EmployeeService employeeService;

    @Mock
    EmployeeRepository employeeRepository;

    @Mock
    DepartmentService departmentService;

    private Department department1;
    private Department department2;
    private Employee employee;
    private Employee newEmployee;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        department1 = new Department();
        department1.setId(1);
        department1.setName("Engineering");

        department2 = new Department();
        department2.setId(2);
        department2.setName("HR");

        employee = new Employee();
        employee.setId(1);
        employee.setFirstName("Saurabh");
        employee.setLastName("Dedhia");
        employee.setSalary(15000);
        employee.setEmail("saurabh.dedhia@freedom.com");
        employee.setDepartment(department1);

        newEmployee = new Employee();
        newEmployee.setSalary(20000);
        newEmployee.setDepartment(department1);
    }

    @Test
    public void testUpdateEmployee_SameDeptPatchSalary() {

        Mockito.when(employeeRepository.findById(1)).thenReturn(Optional.of(employee));
        Mockito.when(employeeRepository.save(employee)).thenReturn(employee);

        Employee updatedEmployee = employeeService
                .updateEmployee(employee.getId(), null, newEmployee, HttpMethod.PATCH);
        Assert.assertNotNull(updatedEmployee);
        Assert.assertEquals(employee.getFirstName(), updatedEmployee.getFirstName());
        Assert.assertEquals(newEmployee.getSalary(), updatedEmployee.getSalary());
    }

    @Test
    public void testUpdateEmployee_DiffDeptPatchSalary() {

        Mockito.when(employeeRepository.findById(1)).thenReturn(Optional.of(employee));
        Mockito.when(employeeRepository.save(employee)).thenReturn(employee);
        Mockito.when(departmentService.getDepartment(2)).thenReturn(Optional.of(department2));

        Employee updatedEmployee = employeeService
                .updateEmployee(employee.getId(), department2.getId(), newEmployee, HttpMethod.PATCH);

        Assert.assertNotNull(updatedEmployee);
        Assert.assertEquals(employee.getFirstName(), updatedEmployee.getFirstName());
        Assert.assertEquals(newEmployee.getSalary(), updatedEmployee.getSalary());
        Assert.assertEquals(department2.getId(), updatedEmployee.getDepartmentId());
    }

    @Test
    public void testUpdateEmployee_SameDeptPutSalary() {

        Mockito.when(employeeRepository.findById(1)).thenReturn(Optional.of(employee));
        Mockito.when(employeeRepository.save(newEmployee)).thenReturn(newEmployee);

        Employee updatedEmployee = employeeService
                .updateEmployee(employee.getId(), null, newEmployee, HttpMethod.PUT);
        Assert.assertNotNull(updatedEmployee);
        Assert.assertEquals(newEmployee.getFirstName(), updatedEmployee.getFirstName());
        Assert.assertEquals(newEmployee.getSalary(), updatedEmployee.getSalary());
    }

    @Test
    public void testUpdateEmployee_DiffDeptPutSalary() {

        Mockito.when(employeeRepository.findById(1)).thenReturn(Optional.of(employee));
        Mockito.when(employeeRepository.save(newEmployee)).thenReturn(newEmployee);
        Mockito.when(departmentService.getDepartment(2)).thenReturn(Optional.of(department2));

        Assert.assertEquals(department1.getId(), newEmployee.getDepartmentId());

        Employee updatedEmployee = employeeService
                .updateEmployee(employee.getId(), department2.getId(), newEmployee, HttpMethod.PUT);

        Assert.assertNotNull(updatedEmployee);
        Assert.assertEquals(newEmployee.getFirstName(), updatedEmployee.getFirstName());
        Assert.assertEquals(newEmployee.getSalary(), updatedEmployee.getSalary());
        Assert.assertEquals(department2.getId(), updatedEmployee.getDepartmentId());
    }

    @Test(expected = EmployeeNotFoundException.class)
    public void testUpdateEmployee_EmployeeNotFoundException() {
        employeeService.updateEmployee(employee.getId(), department2.getId(), newEmployee, HttpMethod.PUT);
    }
}
