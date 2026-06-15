package com.task.project_service.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "projects")
@Getter
@ToString
@Setter
public class Project {
    @Id
    private String _id;
    private String name;
    private List<Column> columns = new ArrayList<>(List.of(
            new Column("first","To Do", 0),
            new Column("second","In Progress", 1),
            new Column("thirt","Done", 2)));
    private List<String> taskIds = new ArrayList<>();
    private LocalDateTime createdAt;

    public Project() {}

    public Project(String name) {
        this.name = name;
        this.createdAt = LocalDateTime.now();
    }

    @JsonProperty("_id")
    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

}
