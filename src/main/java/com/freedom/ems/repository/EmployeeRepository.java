package com.freedom.ems.repository;

import com.freedom.ems.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    boolean existsByEmail(String email);

    List<Employee> findByDepartment_Id(int departmentId);

    long countByDepartment_Id(int departmentId);
}
