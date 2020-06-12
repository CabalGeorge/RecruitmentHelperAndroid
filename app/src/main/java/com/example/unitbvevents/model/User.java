package com.example.unitbvevents.model;

import java.util.Set;

public class User {
    private Integer user_id;
    private String username;
    private String password;
    private String email;

    public Set<Event> getUserEventSet() {
        return userEventSet;
    }

    public void setUserEventSet(Set<Event> userEventSet) {
        this.userEventSet = userEventSet;
    }

    private Set<Event> userEventSet;


    public String getUsername() {

        return username;
    }

    public void setUsername(String username) {

        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
