package com.tiger.capstone.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.tiger.capstone.dto.EmployeeDTO;
import com.tiger.capstone.model.Employee;
import com.tiger.capstone.repository.EmployeeRepository;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository repo;

    public List<Employee> getEmployee(String id){
        return repo.findAll();
    }

    public Employee createEmployee(EmployeeDTO dto){
        Employee employee=new Employee();
        employee.setEmployeeEmail(dto.getEmployeeEmail());
        employee.setEmployeeId(dto.getEmployeeId());
        employee.setEmployeeName(dto.getEmployeeName());
        if(repo.existsByEmployeeId(dto.getEmployeeId())){
            return employee;
        }
        else{
            return repo.save(employee);
        }
    }
}

