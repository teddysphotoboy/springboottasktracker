package com.tiger.capstone.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tiger.capstone.model.TaskUsers;

public interface TaskUsersRepository extends JpaRepository<TaskUsers,Integer>{
    List<TaskUsers> findByEmployeeId(String employeeId);
    List<TaskUsers> findByTaskId(String taskId);
    TaskUsers findByEmployeeIdAndTaskId(String employeeId,String taskId);
    void deleteByTaskId(String taskId);
}
