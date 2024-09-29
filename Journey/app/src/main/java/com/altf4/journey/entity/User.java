package com.altf4.journey.entity;
import java.util.*;

public class User implements Representable {
    private UUID id;
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private ArrayList<Goal> goals;

    private static User instance;

    private User(String email, String firstName, String lastName, String password) {
        this.id = UUID.randomUUID();
        this.email = email;
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

    public String getEmail() {
        return this.email;
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

    public Map<String, String> getDatabaseRepresentation() {
        Map<String, String> representation = new HashMap<>();
        representation.put("id", this.id.toString());
        representation.put("email", this.email);
        representation.put("firstName", this.firstName);
        representation.put("lastName", this.lastName);
        representation.put("password", this.password);

//        // Adding goals as a nested object
////        Map<String, String> goalRepresentation = new HashMap<>();
//        for (Goal goal : this.goals) {
//            representation.put(goal.getGoalId().toString(), goal.getDatabaseRepresentation());
//        }
//        representation.put("goals", goalRepresentation.toString());

        return representation;
    }

}