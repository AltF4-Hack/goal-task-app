package com.altf4.journey.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

public class Goal implements Representable {
    private UUID id;
    private String title;
    private LocalDateTime startDate;
    private int taskCompleted;
    private ArrayList<Task> taskList;

    public Goal(String title) {
        this.id = UUID.randomUUID();
        this.title = title;
        this.startDate = LocalDateTime.now();
        this.taskCompleted = 0;
        taskList = new ArrayList<Task>();
    }

    public Goal(String title, ArrayList<Task> taskList) {
        this.id = UUID.randomUUID();
        this.title = title;
        this.startDate = LocalDateTime.now();
        this.taskCompleted = 0;
        this.taskList = taskList;
    }

    public UUID getGoalId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    public LocalDateTime getStartDate() {
        return this.startDate;
    }

    public Double getPercentageCompletion() {
        return (this.taskCompleted / (double) this.taskList.size()) * 100.0;
    }

    public int getNumOfTaskCompleted() {
        return this.taskCompleted;
    }

    public ArrayList<Task> getTaskList() {
        return this.taskList;
    }

    public void markTaskAsCompleted(Task taskToBeCompleted) {
        taskToBeCompleted.checkTask();
        taskCompleted++;
    }

    public Task removeTask(Task taskToBeRemoved) {
        this.taskList.remove(taskToBeRemoved);
        return taskToBeRemoved;
    }

    public void addTask(Task taskToBeAdded) {
        this.taskList.add(taskToBeAdded);
    }

    public String getDatabaseRepresentation() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        sb.append("  \"id\": \"" + this.id + "\",\n");
        sb.append("  \"title\": \"" + this.title + "\",\n");
        sb.append("  \"startDate\": \"" + this.startDate + "\",\n");
        sb.append("  \"taskCompleted\": \"" + this.taskCompleted + "\",\n");

        // Adding tasks as a nested object
        sb.append("  \"taskList\": {\n");
        for (Task task : this.taskList) {
            sb.append("    \"" + task.getTaskId().toString() + "\": " + task.getDatabaseRepresentation() + ",\n");
        }
        // Removing last comma
        if (!this.taskList.isEmpty()) {
            sb.setLength(sb.length() - 2);  // To remove the last comma and newline
            sb.append("\n");
        }
        sb.append("  }\n");

        sb.append("}\n");
        return sb.toString();
    }

}
