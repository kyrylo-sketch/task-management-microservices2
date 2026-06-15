package com.task.project_service.service;


import com.task.project_service.feign.ProjectInterface;
import com.task.project_service.model.Project;
import com.task.project_service.model.ProjectWrapper;
import com.task.project_service.model.TaskWrapper;
import com.task.project_service.repository.ProjectRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.aggregation.DateOperators;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ProjectService {
    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectInterface projectInterface;

    public ResponseEntity<ProjectWrapper> save(Project project){
        log.info("Saving project request: projectName={}", project.getName());
        Project saved = projectRepository.save(project);

        log.info("Saving project successful: projectName={}", project.getName());
        ProjectWrapper projectWrapper = convertProjectToProjectWrapper(saved);
        return new ResponseEntity<>(projectWrapper, HttpStatus.CREATED);

    }

    public ResponseEntity<List<ProjectWrapper>> findAllUsersProjects(List<String> projectIds){
        if(projectIds == null || projectIds.isEmpty()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        log.info("Finding all project by user request: userId={}", );
        List<Project> projects = new ArrayList<>();
        for(String id : projectIds){
            System.out.println(id);
            projectRepository.findById(id).ifPresent(projects::add);
        }
        List<ProjectWrapper> projectForUser = new ArrayList<>();
        for(Project project : projects){
            System.out.println(project.toString());
            ProjectWrapper projectWrapper = convertProjectToProjectWrapper(project);

            projectForUser.add(projectWrapper);
            System.out.println(projectWrapper.toString());
        }

        return new ResponseEntity<>(projectForUser, HttpStatus.OK);
    }

    public ResponseEntity<Project> findById(String id){
        log.info("Finding project by id request: id={}", id);
        Project project = projectRepository.findById(id).orElse(null);
        if(project == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(project, HttpStatus.OK);
    }

    public ResponseEntity<Project> updateProject(Project project){
        log.info("Updating project request: project={}", project);
        Project saved = projectRepository.save(project);
        log.info("Updating project successful: project={}", project);
        return new ResponseEntity<>(saved, HttpStatus.OK);
    }

    public void deleteProject(String id) {
        projectRepository.deleteById(id);
    }

    public ResponseEntity<List<TaskWrapper>> getTasksProject(String projectId) {
        Project project = projectRepository.findById(projectId).orElse(null);
        if(project == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(projectInterface.getProjects(project.getTaskIds()), HttpStatus.OK);
    }

    public ResponseEntity<String> addTask(String taskId, String projectId) {
        Project project = projectRepository.findById(projectId).orElse(null);
        if(project == null || taskId == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        project.getTaskIds().add(taskId);
        projectRepository.save(project);
        return new ResponseEntity<>(projectId,HttpStatus.OK);
    }

    public ResponseEntity<String> deleteTask(String taskId, String projectId) {
        Project project = projectRepository.findById(projectId).orElse(null);
        if(project == null || taskId == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        project.getTaskIds().remove(taskId);
        projectRepository.save(project);
        return new ResponseEntity<>(projectId,HttpStatus.OK);
    }

    public ProjectWrapper convertProjectToProjectWrapper(Project project) {
        ProjectWrapper projectWrapper = new ProjectWrapper();
        projectWrapper.setName(project.getName());
        projectWrapper.setCreatedAt(project.getCreatedAt());
        projectWrapper.set_id(project.get_id());
        projectWrapper.setColumns(project.getColumns());
        projectWrapper.setTaskIds(project.getTaskIds());

        return projectWrapper;
    }
}
