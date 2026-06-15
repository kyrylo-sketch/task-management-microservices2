package com.task.comment_service.service;

import com.task.comment_service.model.Comment;
import com.task.comment_service.model.CommentWrapper;
import com.task.comment_service.repository.CommentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private CommentService commentService;

    private Comment comment;

    @BeforeEach
    void setUp() {
        comment = new Comment();
        comment.set_id("comment-1");
        comment.setContent("Test comment");
        comment.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void shouldSaveComment() {
        when(commentRepository.save(comment)).thenReturn(comment);

        ResponseEntity<Comment> response =
                commentService.save(comment);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(comment, response.getBody());

        verify(commentRepository).save(comment);
    }

    @Test
    void shouldRemoveComment() {
        commentService.remove("comment-1");

        verify(commentRepository).deleteById("comment-1");
    }

    @Test
    void shouldFindAllTaskComments() {

        Comment secondComment = new Comment();
        secondComment.set_id("comment-2");
        secondComment.setContent("Second comment");
        secondComment.setCreatedAt(LocalDateTime.now());

        when(commentRepository.findById("comment-1"))
                .thenReturn(Optional.of(comment));

        when(commentRepository.findById("comment-2"))
                .thenReturn(Optional.of(secondComment));

        ResponseEntity<List<CommentWrapper>> response =
                commentService.findAllTaskComments(
                        List.of("comment-1", "comment-2")
                );

        assertEquals(HttpStatus.OK, response.getStatusCode());

        List<CommentWrapper> wrappers = response.getBody();

        assertNotNull(wrappers);
        assertEquals(2, wrappers.size());

        assertEquals("comment-1", wrappers.get(0).get_id());
        assertEquals("Test comment", wrappers.get(0).getContent());

        assertEquals("comment-2", wrappers.get(1).get_id());
        assertEquals("Second comment", wrappers.get(1).getContent());

        verify(commentRepository).findById("comment-1");
        verify(commentRepository).findById("comment-2");
    }
}
