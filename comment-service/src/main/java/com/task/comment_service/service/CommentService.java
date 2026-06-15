package com.task.comment_service.service;

import com.task.comment_service.model.Comment;
import com.task.comment_service.model.CommentWrapper;
import com.task.comment_service.repository.CommentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;



    public ResponseEntity<Comment> save(Comment comment){
        log.info("Saving comment request: comment{}", comment);
        Comment saved = commentRepository.save(comment);

        return new ResponseEntity<>(saved, HttpStatus.OK);
    }



    public void  remove(String commentId){
        log.info("Removing comment request: commentId={}", commentId);
        commentRepository.deleteById(commentId);
    }

    public ResponseEntity<List<CommentWrapper>> findAllTaskComments(List<String> commentIds) {

        List<Comment> comments = new ArrayList<>();
        for (String commentId : commentIds) {
            Comment comment = commentRepository.findById(commentId).orElse(null);
            comments.add(comment);
        }

        List<CommentWrapper> commentWrappers = new ArrayList<>();
        for (Comment comment : comments) {
            CommentWrapper commentWrapper = new CommentWrapper();
            commentWrapper.set_id(comment.get_id());
            commentWrapper.setContent(comment.getContent());
            commentWrapper.setCreatedAt(comment.getCreatedAt());
            commentWrappers.add(commentWrapper);
        }

        return new ResponseEntity<>(commentWrappers, HttpStatus.OK);
    }
}
