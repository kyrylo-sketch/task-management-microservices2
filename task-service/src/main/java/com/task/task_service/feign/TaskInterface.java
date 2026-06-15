package com.task.task_service.feign;

import com.task.task_service.model.CommentWrapper;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient("COMMENT-SERVICE")
public interface TaskInterface {
    @PostMapping("/api/comment/allComments")
    public ResponseEntity<List<CommentWrapper>> getComments(@RequestBody List<String> commentIds);
}
