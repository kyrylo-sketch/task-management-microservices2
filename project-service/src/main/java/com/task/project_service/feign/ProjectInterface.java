package com.task.project_service.feign;

import com.task.project_service.model.TaskWrapper;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient("TASK-SERVICE")
public interface ProjectInterface {
    @PostMapping("/api/tasks/allTasks")
    public List<TaskWrapper> getProjects(@RequestBody List<String> taskIds);

}
