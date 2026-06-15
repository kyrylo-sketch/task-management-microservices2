package com.task.project_service.service;

import com.task.project_service.feign.ProjectInterface;
import com.task.project_service.model.Project;
import com.task.project_service.model.ProjectWrapper;
import com.task.project_service.model.TaskWrapper;
import com.task.project_service.repository.ProjectRepository;
import org.apache.catalina.User;
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
public class ProjectServiceTest {
    @Mock
    ProjectRepository projectRepository;

    @Mock
    ProjectInterface projectInterface;

    @InjectMocks
    ProjectService projectService;

    Project project;

    @BeforeEach
    void setUp() {
        project = new Project();
        project.set_id("proj_id");
        project.setName("proj_name");
    }

    @Test
    void save_shouldSaveAndReturnIt(){
        //arrange
        when(projectRepository.save(project)).thenReturn(project);

        //act
        ResponseEntity<ProjectWrapper> save = projectService.save(project);
        ProjectWrapper result = save.getBody();

        //assert
        assertNotNull(result);
        verify(projectRepository, times(1)).save(project);
    }


    @Test
    void findAllUser_shouldReturnProjects(){
        List<String> projectIds = new ArrayList<>();
        projectIds.add("proj_id");

        when(projectRepository.findById("proj_id")).thenReturn(Optional.of(project));

        ResponseEntity<List<ProjectWrapper>> allUsersProjects = projectService.findAllUsersProjects(projectIds);
        List<ProjectWrapper> result = allUsersProjects.getBody();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(projectRepository, times(1)).findById("proj_id");
    }

    @Test
    void findAllUsersProjects_shouldReturnEmptyList_whenUserNotFound(){
        List<String> projectIds = new ArrayList<>();

        ResponseEntity<List<ProjectWrapper>> result = projectService.findAllUsersProjects(projectIds);


        assertTrue(projectIds.isEmpty());
        verify(projectRepository, never()).findById("proj_id");
    }

    @Test
    void findById_shouldReturnIt(){
        when(projectRepository.findById("proj_id")).thenReturn(Optional.of(project));

        ResponseEntity<Project> projId = projectService.findById("proj_id");
        Project result = projId.getBody();

        assertEquals(project, result);
        verify(projectRepository, times(1)).findById("proj_id");

    }

    @Test
    void findById_shouldReturnNull_whenProjectNotFound(){
        when(projectRepository.findById("empty")).thenReturn(Optional.empty());

        ResponseEntity<Project> empty = projectService.findById("empty");
        Project result = empty.getBody();

        assertNull(result);
        verify(projectRepository).findById("empty");
    }

    @Test
    void updateProjet_shouldUpdateIt(){
        Project updated = new Project();
        updated.set_id(project.get_id());
        updated.setName("new name");
        when(projectRepository.save(updated)).thenReturn(updated);

        ResponseEntity<Project> projectResponseEntity = projectService.updateProject(updated);
        Project result = projectResponseEntity.getBody();

        assertEquals("proj_id", result.get_id());
        verify(projectRepository, times(1)).save(updated);
    }

    @Test
    void deleteProject_shouldDeleteIt(){
        projectService.deleteProject("proj_id");

        verify(projectRepository, times(1)).deleteById(project.get_id());
    }

    @Test
    void getTasksProjects_shouldReturnListOfTasks(){
        project.getTaskIds().add("task_id");

        List<String> taskIds = List.of("task_id");
        when(projectRepository.findById("proj_id")).thenReturn(Optional.of(project));
        when(projectInterface.getProjects(taskIds)).thenReturn(List.of(new TaskWrapper()));

        ResponseEntity<List<TaskWrapper>> response = projectService.getTasksProject("proj_id");
        List<TaskWrapper> result = response.getBody();

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void getTasks_shouldBadRequest_whenProjectNotFound(){
        when(projectRepository.findById("empty")).thenReturn(Optional.empty());
        ResponseEntity<List<TaskWrapper>> response = projectService.getTasksProject("empty");

        ResponseEntity<List<TaskWrapper>> empty = projectService.getTasksProject("empty");

        assertTrue(response.equals(empty));
        assertEquals(new ResponseEntity<>(HttpStatus.BAD_REQUEST),  response);

    }

    @Test
    void addTask_shouldAddTask(){
        String taskId = "task_id";
        when(projectRepository.findById("proj_id")).thenReturn(Optional.of(project));
        when(projectRepository.save(project)).thenReturn(project);

        ResponseEntity<String> stringResponseEntity = projectService.addTask(taskId, project.get_id());

        assertEquals(1, project.getTaskIds().size());
        assertEquals("proj_id", stringResponseEntity.getBody());
        verify(projectRepository, times(1)).save(project);
    }

    @Test
    void addTask_shouldNotAddTask_whenProjectNotFound(){
        when(projectRepository.findById("empty")).thenReturn(Optional.empty());

        ResponseEntity<String> stringResponseEntity = projectService.addTask("task_id", "empty");

        assertEquals(0, project.getTaskIds().size());
        assertEquals(new ResponseEntity<>(HttpStatus.BAD_REQUEST), stringResponseEntity);
    }

    @Test
    void deleteTask_shouldDeleteTask(){
        String taskId = "task_id";
        when(projectRepository.findById("proj_id")).thenReturn(Optional.of(project));
        when(projectRepository.save(project)).thenReturn(project);

        ResponseEntity<String> stringResponseEntity = projectService.deleteTask(taskId, project.get_id());

        assertEquals(0, project.getTaskIds().size());
        assertEquals("proj_id", stringResponseEntity.getBody());
        verify(projectRepository, times(1)).save(project);
    }

    @Test
    void deleteTask_shouldNotDeleteTask_whenProjectNotFound(){
        when(projectRepository.findById("empty")).thenReturn(Optional.empty());

        ResponseEntity<String> stringResponseEntity = projectService.deleteTask("task_id", "empty");

        assertEquals(new ResponseEntity<>(HttpStatus.BAD_REQUEST), stringResponseEntity);
    }
}

