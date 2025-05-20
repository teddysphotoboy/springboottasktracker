package com.tiger.capstone.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tiger.capstone.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee,String> {
    boolean existsByEmployeeId(String id);
    List<Employee> findAllByEmployeeId(String employeeId);
    Employee findByEmployeeId(String employeeId);
    
}
