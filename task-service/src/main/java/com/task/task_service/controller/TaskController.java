package com.task.task_service.controller;


import com.task.task_service.model.CommentWrapper;
import com.task.task_service.model.Task;
import com.task.task_service.model.TaskWrapper;
import com.task.task_service.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/tasks")
public class TaskController {
    @Autowired
    private TaskService taskService;

    @PostMapping("/allTasks")
    public ResponseEntity<List<TaskWrapper>> getProjects(@RequestBody List<String> taskIds) {
        return taskService.findAllProjectTasks(taskIds);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable String id) {
        return taskService.findById(id);
    }

    @PostMapping("/addComment")
    public ResponseEntity<String> addComment(@RequestParam String commentId, @RequestParam String taskId){
        return taskService.addComment(commentId, taskId);
    }

    @PostMapping("/deleteComment")
    public ResponseEntity<String> deleteComment(@RequestParam String commentId, @RequestParam String taskId) {
        return taskService.deleteComment(commentId, taskId);
    }

    @PostMapping("/allComments/{taskId}")
    public ResponseEntity<List<CommentWrapper>> getAllComments(@PathVariable String taskId){
        return taskService.getCommentsTask(taskId);
    }

}
