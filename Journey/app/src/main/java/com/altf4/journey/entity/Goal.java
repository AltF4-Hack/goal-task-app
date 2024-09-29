package com.altf4.journey.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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

    public Map<String, Object> getDatabaseRepresentation() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", this.id);
        map.put("title", this.title);
        map.put("startDate", this.startDate);
        map.put("taskCompleted", this.taskCompleted);
        Map<String, Object> nestedMap = new HashMap<>();
        for (Task task : this.taskList) {
            nestedMap.put(task.getTaskId().toString(), task.getDatabaseRepresentation());
        }
        map.put("taskList", nestedMap);
        return map;
    }
}
