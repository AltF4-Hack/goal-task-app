package com.altf4.journey.entity;
import java.util.*;

public class User implements Representable {
    private UUID id;
    private String username;
    private String firstName;
    private String lastName;
    private String password;
    private ArrayList<Goal> goals;

    public User(String username, String firstName, String lastName, String password) {
        this.id = UUID.randomUUID();
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.goals = new ArrayList<Goal>();
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

    public Map<String, Object> getDatabaseRepresentation() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", this.id);
        map.put("username", this.username);
        map.put("firstName", this.firstName);
        map.put("lastName", this.lastName);
        map.put("password", this.password);
        Map<String, Object> nestedMap = new HashMap<>();
        for (Goal goal : this.goals) {
            nestedMap.put(goal.getGoalId().toString(), goal.getDatabaseRepresentation());
        }
        map.put("goals", nestedMap);
        return map;
    }
}