package com.freedom.ems.controller;

import com.freedom.ems.exception.CustomResponse;
import com.freedom.ems.exception.EmployeeNotFoundException;
import com.freedom.ems.model.Employee;
import com.freedom.ems.service.EmployeeService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class EmployeeController {

    @Autowired
    EmployeeService employeeService;

    @GetMapping("/employees")
    @ApiOperation(value = "Get all Employees")
    @ApiImplicitParam(name = "Authorization", required = true, paramType = "header")
    public ResponseEntity<List<Employee>> getAllEmployees() {
        List<Employee> employees = employeeService.getAllEmployees();
        return new ResponseEntity<>(employees, employees.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK);
    }

    @GetMapping("/employees/department/{departmentId}")
    @ApiOperation(value = "Get all Employees in a Department")
    @ApiImplicitParam(name = "Authorization", required = true, paramType = "header")
    public ResponseEntity<List<Employee>> getAllEmployeesByDepartment(@PathVariable int departmentId) {
        List<Employee> employees = employeeService.getAllEmployeesByDepartment(departmentId);
        return new ResponseEntity<>(employees, employees.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK);
    }

    @GetMapping("/employee/{id}")
    @ApiOperation(value = "Get Employee by ID")
    @ApiImplicitParam(name = "Authorization", required = true, paramType = "header")
    public Employee getEmployee(@PathVariable int id) {
        return employeeService.getEmployee(id).orElseThrow(EmployeeNotFoundException::new);
    }

    @PostMapping("/employee/department/{departmentId}")
    @ApiOperation(value = "Create Employee in a Department")
    @ApiImplicitParam(name = "Authorization", required = true, paramType = "header")
    public ResponseEntity<Employee> createEmployee(@PathVariable int departmentId,
                                                   @RequestBody @Valid Employee employee) {
        return new ResponseEntity<>(employeeService.createEmployee(departmentId, employee), HttpStatus.CREATED);
    }

    @PutMapping("/employee/{id}/department/{departmentId}")
    @ApiOperation(value = "Update Employee in a Department")
    @ApiImplicitParam(name = "Authorization", required = true, paramType = "header")
    public ResponseEntity<Employee> updateEmployee(@PathVariable int id, @PathVariable int departmentId,
                                                   @RequestBody Employee employee) {
        return ResponseEntity.ok(employeeService.updateEmployee(id, departmentId, employee, HttpMethod.PUT));
    }

    @PatchMapping("/employee/{id}")
    @ApiOperation(value = "Update Specific Properties of an Employee (Patch)")
    @ApiImplicitParam(name = "Authorization", required = true, paramType = "header")
    public ResponseEntity<Employee> updateEmployee(@PathVariable int id,
                                                   @RequestParam(required = false) Integer departmentId,
                                                   @RequestBody Employee employee) {
        return ResponseEntity.ok(employeeService.updateEmployee(id, departmentId, employee, HttpMethod.PATCH));
    }

    @DeleteMapping("/employee/{id}")
    @ApiOperation(value = "Delete Employee")
    @ApiImplicitParam(name = "Authorization", required = true, paramType = "header")
    public ResponseEntity<Object> deleteEmployee(@PathVariable int id) {
        employeeService.getEmployee(id).orElseThrow(EmployeeNotFoundException::new);
        employeeService.deleteEmployee(id);
        return new ResponseEntity<>(new CustomResponse(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
                "Employee Deleted Successfully"), HttpStatus.OK);
    }
}
