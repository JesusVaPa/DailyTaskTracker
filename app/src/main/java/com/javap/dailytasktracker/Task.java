package com.javap.dailytasktracker;

import java.util.HashMap;
import java.util.Map;

public class Task {
    private String id;
    private String name;
    private String dueDate;
    private String priority;
    private boolean isCompleted;

    public Task() {

    }

    public Task(String name, String dueDate, String priority, boolean isCompleted) {
        this.name = name;
        this.dueDate = dueDate;
        this.priority = priority;
        this.isCompleted = isCompleted;
    }

    public Task(String id, String name, String dueDate, String priority, boolean isCompleted) {
        this.id = id;
        this.name = name;
        this.dueDate = dueDate;
        this.priority = priority;
        this.isCompleted = isCompleted;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDueDate() { return dueDate; }
    public void setDueDate(String dueDate) { this.dueDate = dueDate; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }

    public boolean isCompleted() { return isCompleted; }
    public void setCompleted(boolean completed) { isCompleted = completed; }

    // Convert Task to Map for use it in Firestore
    public Map<String, Object> toMap() {
        Map<String, Object> taskMap = new HashMap<>();
        taskMap.put("name", name);
        taskMap.put("dueDate", dueDate);
        taskMap.put("priority", priority);
        taskMap.put("isCompleted", isCompleted);
        return taskMap;
    }
}
