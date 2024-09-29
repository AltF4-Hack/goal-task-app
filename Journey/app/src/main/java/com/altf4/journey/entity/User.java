package com.altf4.journey.entity;
import java.util.*;

public class User implements Representable {
    private UUID id;
    private String username;
    private String firstName;
    private String lastName;
    private String password;
    private ArrayList<Goal> goals;

    private static User instance;

    private User(String username, String firstName, String lastName, String password) {
        this.id = UUID.randomUUID();
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.goals = new ArrayList<Goal>();
    }

    public static User getInstance(String username, String firstName, String lastName, String password) {
        if (instance == null) {
            instance = new User(username, firstName, lastName, password);
        }
        return instance;
    }

    public UUID getUserId() {
        return this.id;
    }

    public String getUsername() {
        return this.username;
    }
    public String getFirstName() {
        return this.firstName;
    }
    public String getLastName() {
        return this.lastName;
    }

    public String getPassword() {
        return this.password;
    }

    public ArrayList<Goal> getGoals() {
        return this.goals;
    }

    public void addGoal(Goal goalToBeAdded) {
        this.goals.add(goalToBeAdded);
    }

    public Goal removeGoal(Goal goalToBeRemoved) {
        this.goals.remove(goalToBeRemoved);

        return goalToBeRemoved;
    }

    public String getDatabaseRepresentation() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        sb.append("  \"id\": \"" + this.id + "\",\n");
        sb.append("  \"username\": \"" + this.username + "\",\n");
        sb.append("  \"firstName\": \"" + this.firstName + "\",\n");
        sb.append("  \"lastName\": \"" + this.lastName + "\",\n");
        sb.append("  \"password\": \"" + this.password + "\",\n");

        // Adding goals as a nested object
        sb.append("  \"goals\": {\n");
        for (Goal goal : this.goals) {
            sb.append("    \"" + goal.getGoalId().toString() + "\": " + goal.getDatabaseRepresentation() + ",\n");
        }
        // Removing last comma
        if (!this.goals.isEmpty()) {
            sb.setLength(sb.length() - 2);  // To remove the last comma and newline
            sb.append("\n");
        }
        sb.append("  }\n");

        sb.append("}\n");
        return sb.toString();
    }

}