package com.altf4.journey.entity;

import android.os.Build;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Task implements Representable {
    private UUID id;
    private String title;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Boolean isComplete;
    public Task(String title, String description, LocalDateTime endDate) {
        this.id = UUID.randomUUID();
        this.title = title;
        this.description = description;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            this.startDate = LocalDateTime.now();
        }
        this.endDate = endDate;
        this.isComplete = false;
    }

    public UUID getTaskId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }

    public LocalDateTime getStartDate() {
        return this.startDate;
    }

    public LocalDateTime getEndDate() {
        return this.endDate;
    }

    public Boolean getCompletionStatus() {
        return this.isComplete;
    }

    public void updateEndDate(LocalDateTime newEndDate) {
        this.endDate = newEndDate;
    }

    public void checkTask() {
        this.isComplete = true;
    }

    public void unCheckTask() {
        this.isComplete = false;
    }

    // HashMap-based representation
    public Map<String, Object> getDatabaseRepresentation() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", this.id.toString());
        map.put("title", this.title);
        map.put("description", this.description);
        map.put("startDate", this.startDate.toString());
        map.put("endDate", this.endDate.toString());
        map.put("isComplete", this.isComplete);
        return map;
    }
}
