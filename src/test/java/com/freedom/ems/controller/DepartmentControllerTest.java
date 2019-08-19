package com.freedom.ems.controller;

import com.freedom.ems.exception.DepartmentNotFoundException;
import com.freedom.ems.model.Department;
import com.freedom.ems.service.DepartmentService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class DepartmentControllerTest {

    @Mock
    DepartmentService departmentService;

    @InjectMocks
    DepartmentController departmentController;

    private Department department1;
    private Department department2;
    private List<Department> departments;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        department1 = new Department();
        department1.setId(1);

        department2 = new Department();
        department2.setId(2);

        departments = Arrays.asList(department1, department2);
    }

    @Test
    public void testGetAllDepartments() {
        Mockito.when(departmentService.getAllDepartments()).thenReturn(departments);
        Assert.assertEquals(departments, departmentController.getAllDepartments().getBody());
    }

    @Test
    public void testGetDepartment() {
        Mockito.when(departmentService.getDepartment(1)).thenReturn(Optional.of(department1));
        Assert.assertEquals(department1, departmentController.getDepartment(1));
    }

    @Test(expected = DepartmentNotFoundException.class)
    public void testGetDepartment_DepartmentNotFoundException() {
        departmentController.getDepartment(1);
    }

    @Test
    public void testCreateDepartment() {
        Mockito.when(departmentService.createOrUpdateDepartment(department1)).thenReturn(department1);
        ResponseEntity<Department> departmentEntity = departmentController.createDepartment(department1);
        Assert.assertEquals(department1, departmentEntity.getBody());
        Assert.assertEquals(HttpStatus.CREATED, departmentEntity.getStatusCode());
    }
}
