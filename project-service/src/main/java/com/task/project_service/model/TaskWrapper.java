package com.task.project_service.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TaskWrapper {
        private String _id;
        private String title;
        private String description;
        private Priority priority;
        private LocalDateTime createdAt;
        private String columnId;

        @JsonProperty("_id")
        public String get_id() {
                return _id;
        }
}
