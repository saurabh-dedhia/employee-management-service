package com.freedom.ems.controller;

import com.freedom.ems.exception.CustomResponse;
import com.freedom.ems.exception.DepartmentNotFoundException;
import com.freedom.ems.model.Department;
import com.freedom.ems.service.DepartmentService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class DepartmentController {

    @Autowired
    DepartmentService departmentService;

    @GetMapping("/departments")
    @ApiOperation(value = "Get all Departments")
    @ApiImplicitParam(name = "Authorization", required = true, paramType = "header")
    public ResponseEntity<List<Department>> getAllDepartments() {
        List<Department> departments = departmentService.getAllDepartments();
        return new ResponseEntity<>(departments, departments.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK);
    }

    @GetMapping("/department/{id}")
    @ApiOperation(value = "Get Department by ID")
    @ApiImplicitParam(name = "Authorization", required = true, paramType = "header")
    public Department getDepartment(@PathVariable int id) {
        return departmentService.getDepartment(id).orElseThrow(DepartmentNotFoundException::new);
    }

    @PostMapping("/department")
    @ApiOperation(value = "Create Department")
    @ApiImplicitParam(name = "Authorization", required = true, paramType = "header")
    public ResponseEntity<Department> createDepartment(@RequestBody @Valid Department department) {
        return new ResponseEntity<>(departmentService.createOrUpdateDepartment(department), HttpStatus.CREATED);
    }

    @PutMapping("/department/{id}")
    @ApiOperation(value = "Update Department")
    @ApiImplicitParam(name = "Authorization", required = true, paramType = "header")
    public ResponseEntity<Department> updateDepartment(@PathVariable int id,
                                                       @RequestBody @Valid Department department) {
        department.setId(departmentService.getDepartment(id).orElseThrow(DepartmentNotFoundException::new).getId());
        return ResponseEntity.ok(departmentService.createOrUpdateDepartment(department));
    }

    @DeleteMapping("/department/{id}")
    @ApiOperation(value = "Delete Department")
    @ApiImplicitParam(name = "Authorization", required = true, paramType = "header")
    public ResponseEntity<CustomResponse> deleteDepartment(@PathVariable int id) {
        departmentService.getDepartment(id).orElseThrow(DepartmentNotFoundException::new);
        departmentService.deleteDepartment(id);
        return new ResponseEntity<>(new CustomResponse(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
                "Department Deleted Successfully"), HttpStatus.OK);
    }
}
