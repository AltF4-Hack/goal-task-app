package com.altf4.journey.entity;

import android.os.Build;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    public String getDatabaseRepresentation() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        sb.append("  \"id\": \"" + this.id + "\",\n");
        sb.append("  \"title\": \"" + this.title + "\",\n");
        sb.append("  \"description\": \"" + this.description + "\",\n");
        sb.append("  \"startDate\": \"" + this.startDate + "\",\n");
        sb.append("  \"endDate\": \"" + this.endDate + "\",\n");
        sb.append("  \"isComplete\": \"" + this.isComplete + "\",\n");
        sb.append("}\n");
        return sb.toString();
    }
}
