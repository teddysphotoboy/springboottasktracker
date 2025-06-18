package com.tiger.capstone.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tiger.capstone.dto.EmployeeDTO;
import com.tiger.capstone.model.Employee;
import com.tiger.capstone.model.TaskUsers;
import com.tiger.capstone.model.UserRole;
import com.tiger.capstone.repository.EmployeeRepository;
import com.tiger.capstone.repository.TaskUsersRepository;
import com.tiger.capstone.repository.UserRoleRepository;
import com.tiger.capstone.service.AuthenticationService;
import com.tiger.capstone.service.EmployeeService;

@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EmployeeService service;

    @MockitoBean
    private AuthenticationService authenticationService;

    @MockitoBean
    private EmployeeRepository repo;

    @MockitoBean
    private UserRoleRepository userRoleRepository;

    @MockitoBean
    private TaskUsersRepository taskUsersRepository;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void testGetEmployee() throws Exception {
        Employee emp1 = new Employee("user1", "emp1", "emp1@test.com");
        Employee emp2 = new Employee("user2", "emp2", "emp2@test.com");

        List<Employee> empList = List.of(emp1, emp2);
        when(authenticationService.getGoogleId("Dummy_token")).thenReturn("user1");
        when(service.getEmployee("user1")).thenReturn(empList);

        mockMvc.perform(get("/user/user1").header("authorization", "Bearer Dummy_token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].employee_name").value("emp1"))
                .andExpect(jsonPath("$[1].employee_name").value("emp2"));

    }
    @Test
    void testCreateEmployee() throws Exception{
        EmployeeDTO employeeDTO=new EmployeeDTO("123","emp1","emp1@test.com");

        Employee employee=new Employee("123","emp1","emp1@test.com");
        when(authenticationService.getGoogleId("token")).thenReturn("123");
        when(service.createEmployee(employeeDTO)).thenReturn(employee);
        
        mockMvc.perform(post("/create-user/123")
        .header("authorization", "Bearer token")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(employeeDTO)))
        .andExpect(status().isCreated());

    }
    @Test
    void testGetAssignedUser() throws Exception{
        UserRole ur1=new UserRole(1,5,"eid1","pid2");
        UserRole ur2=new UserRole(2,2,"eid2","pid2");
        UserRole ur3=new UserRole(3,3,"eid3","pid2");
        UserRole ur4=new UserRole(4,2,"eid4","pid2");

        List<UserRole> userRoles=List.of(ur1,ur2,ur3,ur4);

        Employee emp1=new Employee("eid1","emp1","emp1@test.com");
        Employee emp2=new Employee("eid2","emp2","emp2@test.com");
        Employee emp3=new Employee("eid3","emp3","emp3@test.com");
        Employee emp4=new Employee("eid4","emp4","emp4@test.com");
        Employee emp5=new Employee("eid5","emp5","emp5@test.com");



        when(userRoleRepository.findByProjectId("pid2")).thenReturn(userRoles);
        when(repo.findAllByEmployeeId("emp1")).thenReturn(List.of(emp1));
        when(repo.findAllByEmployeeId("emp2")).thenReturn(List.of(emp2));
        when(repo.findAllByEmployeeId("emp3")).thenReturn(List.of(emp3));
        when(repo.findAllByEmployeeId("emp4")).thenReturn(List.of(emp4));
        when(repo.findAllByEmployeeId("emp5")).thenReturn(List.of(emp5));
        
        mockMvc.perform(get("/user/2/eid2/pid2"))
        .andExpect(jsonPath("$.length()").value(3));
        
    }

    @Test
    void testGetReadOnlyUsers() throws Exception{
        List<TaskUsers> taskUsers;
        TaskUsers user1=new TaskUsers(1,"t1","emp1");
        TaskUsers user2=new TaskUsers(2,"t1","emp2");
        TaskUsers user3=new TaskUsers(3,"t1","emp3");
        taskUsers=List.of(user1,user2,user3);
        when(taskUsersRepository.findByTaskId("t1")).thenReturn(taskUsers);
        Employee emp1=new Employee("eid1","emp1","emp1@test.com");
        Employee emp2=new Employee("eid2","emp2","emp2@test.com");
        Employee emp3=new Employee("eid3","emp3","emp3@test.com");

        when(repo.findByEmployeeId("emp1")).thenReturn(emp1);
        when(repo.findByEmployeeId("emp2")).thenReturn(emp2);
        when(repo.findByEmployeeId("emp3")).thenReturn(emp3);

        mockMvc.perform(get("/project/read-only-users/p1/t1/emp1")).andExpect(jsonPath("$.length()").value(3));
        
    }
}
