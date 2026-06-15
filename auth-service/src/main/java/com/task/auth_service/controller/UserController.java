package com.task.auth_service.controller;

import com.task.auth_service.model.ProjectWrapper;
import com.task.auth_service.model.User;
import com.task.auth_service.service.UserService;
import jakarta.ws.rs.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable String id){
        return userService.getUserById(id);
    }

    @PostMapping("/addProject")
    public ResponseEntity<String> addProject(@RequestParam String projectid, @RequestParam String userId){
        return userService.addProjectId(projectid, userId);
    }

    @GetMapping("/allProjects/{id}")
    public ResponseEntity<List<ProjectWrapper>> getAllProjects(@PathVariable String id){
        return userService.getProjectUser(id);
    }
}
