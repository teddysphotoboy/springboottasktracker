package com.tiger.capstone.service;

import com.tiger.capstone.dto.EmployeeDTO;
import com.tiger.capstone.exception.DatabaseException;
import com.tiger.capstone.exception.ResourceNotFoundException;
import com.tiger.capstone.model.Employee;
import com.tiger.capstone.repository.EmployeeRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class EmployeeServiceTest {

    @Autowired
    private EmployeeService employeeService;

    @MockitoBean
    private EmployeeRepository employeeRepository;

    @Test
    void testGetEmployee_ReturnsEmployeeList() {
        Employee emp = new Employee();
        emp.setEmployeeId("emp1");
        emp.setEmployeeName("Alice");
        emp.setEmployeeEmail("alice@example.com");

        when(employeeRepository.findAll()).thenReturn(List.of(emp));

        List<Employee> result = employeeService.getEmployee("any");

        assertEquals(1, result.size());
        assertEquals("Alice", result.get(0).getEmployeeName());
    }

    @Test
    void testGetEmployee_ThrowsResourceNotFound_WhenEmptyList() {
        when(employeeRepository.findAll()).thenReturn(Collections.emptyList());

        assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.getEmployee("any");
        });
    }

    @Test
    void testGetEmployee_ThrowsDatabaseException_OnException() {
        when(employeeRepository.findAll()).thenThrow(new DatabaseException("DB down"));

        assertThrows(DatabaseException.class, () -> {
            employeeService.getEmployee("any");
        });
    }

    @Test
    void testCreateEmployee_Success() {
        EmployeeDTO dto = new EmployeeDTO();
        dto.setEmployeeId("emp2");
        dto.setEmployeeName("Bob");
        dto.setEmployeeEmail("bob@example.com");

        Employee saved = new Employee();
        saved.setEmployeeId(dto.getEmployeeId());
        saved.setEmployeeName(dto.getEmployeeName());
        saved.setEmployeeEmail(dto.getEmployeeEmail());

        when(employeeRepository.save(any(Employee.class))).thenReturn(saved);

        Employee result = employeeService.createEmployee(dto);

        assertEquals("Bob", result.getEmployeeName());
    }

    @Test
    void testCreateEmployee_ThrowsDatabaseException_WhenSaveReturnsNull() {
        EmployeeDTO dto = new EmployeeDTO();
        dto.setEmployeeId("emp3");
        dto.setEmployeeName("Charlie");
        dto.setEmployeeEmail("charlie@example.com");

        when(employeeRepository.save(any(Employee.class))).thenReturn(null);

        assertThrows(DatabaseException.class, () -> {
            employeeService.createEmployee(dto);
        });
    }

    @Test
    void testCreateEmployee_ThrowsDatabaseException_OnException() {
        EmployeeDTO dto = new EmployeeDTO();
        dto.setEmployeeId("emp4");
        dto.setEmployeeName("Dave");
        dto.setEmployeeEmail("dave@example.com");

        when(employeeRepository.save(any(Employee.class)))
                .thenThrow(new DatabaseException("DB write error"));

        assertThrows(DatabaseException.class, () -> {
            employeeService.createEmployee(dto);
        });
    }
}
