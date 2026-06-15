package com.task.task_service.model;

public class MoveTaskRequest {
    private String columnId;
    private String taskId;

    public MoveTaskRequest() {
    }

    public MoveTaskRequest(String columnId, String taskId) {
        this.columnId = columnId;
        this.taskId = taskId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getColumnId() {
        return columnId;
    }

    public void setColumnId(String columnId) {
        this.columnId = columnId;
    }
}
