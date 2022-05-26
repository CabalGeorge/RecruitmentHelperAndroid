package com.example.recruitmenthelper.model;

public class User {
    private Integer user_id;
    private String username;
    private String password;
    private String email;
    private String role;

    public User(Integer user_id, String username, String email, String role) {
        this.user_id = user_id;
        this.username = username;
        this.email = email;
        this.role = role;
    }

    public User(){}

    public String getUsername() {

        return username;
    }

    public void setUsername(String username) {

        this.username = username;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
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
