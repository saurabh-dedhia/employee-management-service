package com.freedom.ems.service;

import com.freedom.ems.exception.IntegrityConstraintViolationException;
import com.freedom.ems.model.Department;
import com.freedom.ems.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class DepartmentService {

    @Autowired
    public DepartmentRepository departmentRepository;

    @Autowired
    EmployeeService employeeService;

    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    public Optional<Department> getDepartment(int id) {
        return departmentRepository.findById(id);
    }

    @Transactional
    public Department createOrUpdateDepartment(Department department) {
        if (departmentRepository.existsByName(department.getName())) {
            throw new IntegrityConstraintViolationException("Department name already exists (should be unique)");
        }
        return departmentRepository.save(department);
    }

    @Transactional
    public void deleteDepartment(int id) {
        long employeeCount = employeeService.getEmployeeCountByDepartment(id);

        if (employeeCount == 0) {
            departmentRepository.deleteById(id);
        } else {
            throw new IntegrityConstraintViolationException(String.format("Department contains %d employees (should " +
                    "be zero)", employeeCount));
        }
    }
}
