package com.task.task_service.service;

import com.task.task_service.feign.TaskInterface;
import com.task.task_service.model.*;
import com.task.task_service.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskInterface taskInterface;

    @InjectMocks
    private TaskService taskService;

    private Task task;

    @BeforeEach
    void setUp() {
        task = new Task();
        task.set_id("1");
        task.setTitle("Test Task");
        task.setDescription("Description");
        task.setColumnId("column1");
        task.setCommentIds(new ArrayList<>());
    }

    @Test
    void shouldSaveTask() {
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        TaskWrapper result = taskService.save(task);

        assertNotNull(result);
        assertEquals(task.get_id(), result.get_id());

        verify(taskRepository).save(task);
    }

    @Test
    void shouldFindTaskById() {
        when(taskRepository.findById("1"))
                .thenReturn(Optional.of(task));

        ResponseEntity<Task> response = taskService.findById("1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(task, response.getBody());
    }

    @Test
    void shouldReturnNotFoundWhenTaskDoesNotExist() {
        when(taskRepository.findById("1"))
                .thenReturn(Optional.empty());

        ResponseEntity<Task> response = taskService.findById("1");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void shouldUpdateTask() {
        when(taskRepository.save(task)).thenReturn(task);

        Task result = taskService.update(task);

        assertEquals(task, result);
        verify(taskRepository).save(task);
    }

    @Test
    void shouldDeleteTask() {
        taskService.delete("1");

        verify(taskRepository).deleteById("1");
    }

    @Test
    void shouldMoveTaskToAnotherColumn() {
        Column column = new Column();
        column.set_id("newColumn");

        when(taskRepository.findById("1"))
                .thenReturn(Optional.of(task));

        when(taskRepository.save(any(Task.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        taskService.move(column, "1");

        assertEquals("newColumn", task.getColumnId());
        verify(taskRepository).save(task);
    }

    @Test
    void shouldReturnEmptyListWhenTaskIdsAreNull() {
        ResponseEntity<List<TaskWrapper>> response =
                taskService.findAllProjectTasks(null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    void shouldReturnAllProjectTasks() {
        when(taskRepository.findById("1"))
                .thenReturn(Optional.of(task));

        ResponseEntity<List<TaskWrapper>> response =
                taskService.findAllProjectTasks(List.of("1"));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals("1", response.getBody().get(0).get_id());
    }

    @Test
    void shouldAddComment() {
        when(taskRepository.findById("1"))
                .thenReturn(Optional.of(task));

        ResponseEntity<String> response =
                taskService.addComment("comment1", "1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(task.getCommentIds().contains("comment1"));

        verify(taskRepository).save(task);
    }

    @Test
    void shouldDeleteComment() {
        task.getCommentIds().add("comment1");

        when(taskRepository.findById("1"))
                .thenReturn(Optional.of(task));

        ResponseEntity<String> response =
                taskService.deleteComment("comment1", "1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(task.getCommentIds().contains("comment1"));

        verify(taskRepository).save(task);
    }

    @Test
    void shouldGetCommentsForTask() {
        task.getCommentIds().add("comment1");

        List<CommentWrapper> comments = List.of(new CommentWrapper());

        when(taskRepository.findById("1"))
                .thenReturn(Optional.of(task));

        when(taskInterface.getComments(task.getCommentIds()))
                .thenReturn(new ResponseEntity<>(comments, HttpStatus.OK));

        ResponseEntity<List<CommentWrapper>> response =
                taskService.getCommentsTask("1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());

        verify(taskInterface).getComments(task.getCommentIds());
    }

    @Test
    void shouldConvertTaskToTaskWrapper() {
        TaskWrapper wrapper = taskService.convertToTaskWrapper(task);

        assertEquals(task.get_id(), wrapper.get_id());
        assertEquals(task.getTitle(), wrapper.getTitle());
        assertEquals(task.getDescription(), wrapper.getDescription());
        assertEquals(task.getColumnId(), wrapper.getColumnId());
    }
}