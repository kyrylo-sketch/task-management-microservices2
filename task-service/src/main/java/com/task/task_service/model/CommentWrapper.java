package com.task.task_service.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CommentWrapper {
    private String _id;
    private String content;
    private LocalDateTime createdAt;

    @JsonProperty("_id")
    public String get_id() {
        return _id;
    }
}
