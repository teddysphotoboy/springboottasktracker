package com.tiger.capstone.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.tiger.capstone.dto.EmployeeDTO;
import com.tiger.capstone.model.Employee;
import com.tiger.capstone.model.TaskUsers;
import com.tiger.capstone.model.UserRole;
import com.tiger.capstone.repository.EmployeeRepository;
import com.tiger.capstone.repository.TaskUsersRepository;
import com.tiger.capstone.repository.UserRoleRepository;
import com.tiger.capstone.service.EmployeeService;

@RestController
public class EmployeeController {
    @Autowired
    private EmployeeService service;

    @Autowired
    private EmployeeRepository repo;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private TaskUsersRepository taskUsersRepository;

    @GetMapping("/user/{userId}")
    public List<Employee> getEmployee(@PathVariable String userId){
        return service.getEmployee(userId);
    }
    @PostMapping("/create-user/{userId}")
    public ResponseEntity<Employee> createEmployee(@PathVariable String userId,@RequestBody EmployeeDTO dto){
        Employee employee=service.createEmployee(dto);
        return new ResponseEntity<>(employee,HttpStatus.CREATED);
    }

    @GetMapping("/user/{roleId}/{userId}/{projectId}")
    public List<Employee> getAssignedUser(@PathVariable int roleId,@PathVariable String userId,@PathVariable String projectId){
        List<Employee> users=new ArrayList<>();
        List<UserRole> userRoles=userRoleRepository.findByProjectId(projectId);
        if (userRoles == null || userRoles.isEmpty()) {
            return Collections.emptyList();
        }
        for(UserRole userRole:userRoles){
            if((roleId==2&&userRole.getRoleId()==2)||(roleId!=2&&userRole.getRoleId()==3)){
                List<Employee> userList=repo.findAllByEmployeeId(userRole.getEmployeeId());
                for(Employee emp:userList){
                    users.add(emp);
                }
            }
        }
        return users;
    }

    @GetMapping("/project/read-only-users/{projectId}/{taskId}/{userId}")
    public List<Employee> getReadOnlyUser(@PathVariable String projectId,@PathVariable String taskId,@PathVariable String userId){
        List<Employee> userList=new ArrayList<>();
        List<TaskUsers> taskUsers=taskUsersRepository.findByTaskId(taskId);
        for(TaskUsers taskUser:taskUsers){
            userList.add(repo.findByEmployeeId(taskUser.getEmployeeId()));
        }
        return userList;
    }
}
