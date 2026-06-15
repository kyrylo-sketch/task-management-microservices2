package com.task.project_service.controller;

import com.task.project_service.feign.ProjectInterface;
import com.task.project_service.model.Project;
import com.task.project_service.model.ProjectWrapper;
import com.task.project_service.model.TaskWrapper;
import com.task.project_service.service.ProjectService;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.annotation.AuthenticationPrina
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;


    @PostMapping("/addProject")
    public ResponseEntity<ProjectWrapper> createProject(@RequestBody Project project) {
        return projectService.save(project);
    }


    @PostMapping("/allProjects")
    public ResponseEntity<List<ProjectWrapper>> getProjects(@RequestBody List<String> projectIds) {
        return projectService.findAllUsersProjects(projectIds);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Project> getProjectById(@PathVariable String id){
        return projectService.findById(id);
    }

    @PutMapping("/update")
    public ResponseEntity<Project> updateProject(@RequestBody Project project){
        return projectService.updateProject(project);
    }

    @DeleteMapping("/{id}")
    public void deleteProject(@PathVariable String id){
        projectService.deleteProject(id);
    }

    @PostMapping("/allTasks/{projectId}")
    public ResponseEntity<List<TaskWrapper>> getAllProjectTasks(@PathVariable String projectId) {
        return projectService.getTasksProject(projectId);
    }

    @PostMapping("/addTask")
    public ResponseEntity<String> addTask(@RequestParam String taskId, @RequestParam String projectId) {
        return projectService.addTask(taskId, projectId);
    }

    @PostMapping("/deleteTask")
    public ResponseEntity<String> deleteTask(@RequestParam String taskId, @RequestParam String projectId) {
        return projectService.deleteTask(taskId, projectId);
    }

}

