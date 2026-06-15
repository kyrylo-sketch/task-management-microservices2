package com.task.task_service.controller;


import com.task.task_service.model.MoveTaskRequest;
import com.task.task_service.model.Task;
import com.task.task_service.model.TaskWrapper;
import com.task.task_service.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.Map;

@Controller
public class TaskWebSocketController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("taskManagement.createTask")
    @SendTo("/topic/projects")
    public TaskWrapper createTask(@Payload Task task){
        return taskService.save(task);
    }

    @MessageMapping("taskManagement.updateTask")
    @SendTo("/topic/projects")
    public Task updateTask(@Payload Task task){
        return taskService.update(task);
    }

    @MessageMapping("taskManagement.deleteTask")
    @SendTo("/topic/projects/delete")
    public String deleteTask(@Payload Map<String, String> payload){
        String taskId = payload.get("taskId");
        taskService.delete(taskId);
        return taskId;
    }

    @MessageMapping("taskManagement.moveTask")
    @SendTo("/topic/projects/move")
    public Task moveTask(@Payload MoveTaskRequest request){
        String taskId = request.getTaskId();
        String columnId = request.getColumnId();
        Task task = taskService.findById(taskId).getBody();
        task.setColumnId(columnId);
        return taskService.update(task);
    }
}
