package com.tiger.capstone.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.tiger.capstone.dto.EmployeeDTO;
import com.tiger.capstone.exception.DatabaseException;
import com.tiger.capstone.exception.ResourceNotFoundException;
import com.tiger.capstone.model.Employee;
import com.tiger.capstone.repository.EmployeeRepository;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository repo;

    public List<Employee> getEmployee(String id) {
        try {
            List<Employee> employees = repo.findAll();
            if (employees == null || employees.isEmpty()) {
                throw new ResourceNotFoundException("No employees found.");
            }
            return employees;
        } catch (DataAccessException e) {
            throw new DatabaseException("Error while fetching employees from DB: " + e.getMessage());
        }
    }

    public Employee createEmployee(EmployeeDTO dto) {
        try {

            Employee employee = new Employee();
            employee.setEmployeeEmail(dto.getEmployeeEmail());
            employee.setEmployeeId(dto.getEmployeeId());
            employee.setEmployeeName(dto.getEmployeeName());

            Employee saved = repo.save(employee);
            if (saved == null) {
                throw new DatabaseException("Failed to create employee. Save returned null.");
            }

            return saved;
        } catch (DataAccessException e) {
            throw new DatabaseException("Error while creating employee: " + e.getMessage());
        }
    }
}
