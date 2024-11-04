package com.example.demo.dto;

import java.util.List;

public class TaskRequest {
    private String projectCode;
    private String memberId;
    private List<Task> tasks;

    // Getters and setters
    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    @Override
    public String toString() {
        return "TaskRequest [projectCode=" + projectCode + ", memberId=" + memberId + ", tasks=" + tasks + "]";
    }
}
