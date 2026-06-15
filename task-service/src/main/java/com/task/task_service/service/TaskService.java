package com.task.task_service.service;

import com.task.task_service.feign.TaskInterface;
import com.task.task_service.model.Column;
import com.task.task_service.model.CommentWrapper;
import com.task.task_service.model.Task;
import com.task.task_service.model.TaskWrapper;
import com.task.task_service.repository.TaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskInterface taskInterface;



    public TaskWrapper save(Task task){
        Task saved = taskRepository.save(task);

        TaskWrapper taskWrapper = convertToTaskWrapper(saved);
        return taskWrapper;
    }


    public ResponseEntity<Task> findById(String id){
        log.info("Finding task by id request taskId={}", id);
        Task task = taskRepository.findById(id).orElse(null);
        if(task == null) return new  ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    public Task update(Task updated){
        log.info("Updating task request:  updated={} ",updated);
        return taskRepository.save(updated);
    }

    public void delete(String id){
        log.info("Deleting task by id request taskId={}", id);
        taskRepository.deleteById(id);
    }

    public void move(Column column, String taskId){
        log.info("Moving task request: taskId={}, column={}", taskId, column);
        try{
            ResponseEntity<Task> byId = findById(taskId);
            Task task = byId.getBody();
            task.setColumnId(column.get_id());
            taskRepository.save(task);
        }catch (NullPointerException e){
            log.error("Error NullPointerException: taskId={}, column={}", taskId, column);
            e.printStackTrace();
        }catch (Exception e){
            log.error("Error Exception: taskId={}, column={}", taskId, column);
            e.printStackTrace();
        }
        log.info("Moving task successful: taskId={}, column={}", taskId, column);

    }

    public ResponseEntity<List<TaskWrapper>> findAllProjectTasks(List<String> taskIds) {
        if(taskIds == null || taskIds.isEmpty()) return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        List<Task> tasks = new ArrayList<>();
        for (String taskId : taskIds) {
            System.out.println(taskId);
            Task task = findById(taskId).getBody();
            tasks.add(task);
        }

        List<TaskWrapper> tasksForProject = new ArrayList<>();
        for (Task task : tasks) {
            TaskWrapper taskWrapper = convertToTaskWrapper(task);
            tasksForProject.add(taskWrapper);
        }

        return new ResponseEntity<>(tasksForProject, HttpStatus.OK);
    }

    public TaskWrapper convertToTaskWrapper(Task task){
        TaskWrapper taskWrapper = new TaskWrapper();
        taskWrapper.set_id(task.get_id());
        taskWrapper.setColumnId(task.getColumnId());
        taskWrapper.setTitle(task.getTitle());
        taskWrapper.setDescription(task.getDescription());
        taskWrapper.setCreatedAt(task.getCreatedAt());
        taskWrapper.setPriority(task.getPriority());
        return taskWrapper;
    }

    public ResponseEntity<String> addComment(String commentId, String taskId) {
        Task task = findById(taskId).getBody();
        task.getCommentIds().add(commentId);
        taskRepository.save(task);
        return new ResponseEntity<>(commentId, HttpStatus.OK);
    }

    public ResponseEntity<String> deleteComment(String commentId, String taskId) {
        Task task = findById(taskId).getBody();
        task.getCommentIds().remove(commentId);
        taskRepository.save(task);
        return new ResponseEntity<>(commentId, HttpStatus.OK);
    }

    public ResponseEntity<List<CommentWrapper>> getCommentsTask(String taskId) {
        List<String> commentIds = findById(taskId).getBody().getCommentIds();
        return taskInterface.getComments(commentIds);
    }
}
