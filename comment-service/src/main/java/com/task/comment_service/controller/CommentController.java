package com.task.comment_service.controller;


import com.task.comment_service.model.Comment;
import com.task.comment_service.model.CommentWrapper;
import com.task.comment_service.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @PostMapping("/addComment")
    public ResponseEntity<Comment> addComment(@RequestBody Comment comment) {
        return commentService.save(comment);
    }

    @PostMapping("/allComments")
    public ResponseEntity<List<CommentWrapper>> getComments(@RequestBody List<String> commentIds) {
        return commentService.findAllTaskComments(commentIds);
    }

    @DeleteMapping("/{commentId}")
    public void deleteComment(@PathVariable(name = "commentId") String commentId){
        commentService.remove(commentId);
    }
}
