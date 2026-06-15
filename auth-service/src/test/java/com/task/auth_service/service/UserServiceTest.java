package com.task.auth_service.service;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.task.auth_service.feign.UserInterface;
import com.task.auth_service.model.User;
import com.task.auth_service.repository.UserRepository;
import com.task.auth_service.security.JWTService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserInterface userInterface;

    @InjectMocks
    UserService userService;

    User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.set_id("user_id");
        user.setName("name");
        user.setEmail("email");
        user.setPassword("password");
    }

    @Test
    void addProjectId_shouldIdsAreCorrectly() {
        String projectId = "projectId";
        when(userRepository.existsById(user.get_id())).thenReturn(true);
        when(userRepository.findById(user.get_id())).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        //act
        ResponseEntity<String> stringResponseEntity = userService.addProjectId(projectId, user.get_id());

        assertEquals(new ResponseEntity<>(projectId, HttpStatus.OK), stringResponseEntity);
    }

    @Test
    void addProjectId_shouldNotAddProjectId_ifProjectIdIsNull() {
        String projectId = "";
        when(userRepository.existsById(user.get_id())).thenReturn(true);

        ResponseEntity<String> stringResponseEntity = userService.addProjectId(projectId, user.get_id());

        assertEquals(new ResponseEntity<>(HttpStatus.BAD_REQUEST), stringResponseEntity);
    }
}
