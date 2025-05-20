package com.tiger.capstone.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tiger.capstone.model.Project;

public interface ProjectRepository extends JpaRepository<Project,String>{
    Project findByProjectId(String projectId);
}
