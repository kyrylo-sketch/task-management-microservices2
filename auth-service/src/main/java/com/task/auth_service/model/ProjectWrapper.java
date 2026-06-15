package com.task.auth_service.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectWrapper {
    private String _id;
    private String name;
    private List<Column> columns = new ArrayList<>(List.of(
            new Column("first","To Do", 0),
            new Column("second","In Progress", 1),
            new Column("thirt","Done", 2)));
    private List<String> taskIds = new ArrayList<>();
    private LocalDateTime createdAt;

    @JsonProperty("_id")
    public String get_id() {
        return _id;
    }
}

