package com.task.auth_service.service;

import com.task.auth_service.feign.UserInterface;
import com.task.auth_service.model.ProjectWrapper;
import com.task.auth_service.model.User;
import com.task.auth_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserInterface userInterface;

    public ResponseEntity<User> getUserById(String id) {
        User user = userRepository.findById(id).orElseThrow();
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    public ResponseEntity<String> addProjectId(String projectId, String userId) {
        if(!userRepository.existsById(userId) || projectId.isEmpty() || userId.isEmpty()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        User user = userRepository.findById(userId).orElseThrow();
        user.getProjectIds().add(projectId);
        userRepository.save(user);
        return new ResponseEntity<>(projectId, HttpStatus.OK);
    }

    public ResponseEntity<List<ProjectWrapper>> getProjectUser(String id) {
        if(!userRepository.existsById(id) || id.isEmpty()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        User user = userRepository.findById(id).orElseThrow();
        List<ProjectWrapper> projects = userInterface.getProjects(user.getProjectIds());
        return new ResponseEntity<>(projects, HttpStatus.OK);
    }
}
