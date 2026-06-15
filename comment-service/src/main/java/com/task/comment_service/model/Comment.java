package com.task.comment_service.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;


@Getter
@Setter
@ToString
@Document(collection = "comments")
public class Comment {
    @Id
    @JsonProperty("_id")
    private String _id;
    private String content;
    private String userId;
    private LocalDateTime createdAt;

    public Comment() {
    }
    public Comment(String content, String authorId) {
        this.content = content;
        this.userId = authorId;
    }

    @JsonProperty("_id")
    public String get_id() {
        return _id;
    }
}
