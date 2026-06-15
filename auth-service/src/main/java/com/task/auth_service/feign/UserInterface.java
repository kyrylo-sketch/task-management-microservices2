package com.task.auth_service.feign;

import com.task.auth_service.model.ProjectWrapper;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient("PROJECT-SERVICE")
public interface UserInterface {
    @PostMapping("/api/projects/allProjects")
    public List<ProjectWrapper> getProjects(@RequestBody List<String> projectIds);
}
