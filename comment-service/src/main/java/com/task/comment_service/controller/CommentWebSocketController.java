package com.task.comment_service.controller;

import com.task.comment_service.model.Comment;
import com.task.comment_service.model.CommentMessage;
import com.task.comment_service.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Controller
public class CommentWebSocketController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/taskManagement.addComment")
    public void addComment(@Payload CommentMessage message) {
        Comment comment = message.getComment();
        comment.setCreatedAt(LocalDateTime.now());
        Comment saved = commentService.save(comment).getBody();

        messagingTemplate.convertAndSend(
                "/topic/comments/" + message.getTaskId(),
                saved
        );
    }
}
