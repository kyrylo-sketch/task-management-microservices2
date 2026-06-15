package com.task.task_service.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ToString
@Document(collection = "tasks")
@Getter
@Setter
public class Task {
    @Id
    @JsonProperty("_id")
    private String _id;
    private String title;
    private String description;
    private Priority priority;
    private LocalDateTime deadline;
    private List<String> commentIds = new ArrayList<>();
    private LocalDateTime createdAt;
    private String columnId;

    public Task() {}

    public Task(String title, String description, Priority priority, LocalDateTime deadline, String columnId) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.deadline = deadline;
        this.createdAt = LocalDateTime.now();
        this.columnId = columnId;
    }

    @JsonProperty("_id")
    public String get_id() {
        return _id;
    }


}
