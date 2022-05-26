package com.example.recruitmenthelper.model;


import java.util.List;

public class Event {

    private String name;
    private String dateTime;
    private String location;
    private String seats;
    private List<User> userList;

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    public String getSeats() {
        return seats;
    }

    public void setSeats(String seats) {
        this.seats = seats;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Event(String name, String dateTime, String location, String seats){
        this.name=name;
        this.dateTime=dateTime;
        this.location=location;
        this.seats=seats;
    }

    public Event(){}

    @Override
    public String toString() {
        return "Event{" +
                "name='" + name + '\'' +
                ", dateTime='" + dateTime + '\'' +
                ", location='" + location + '\'' +
                ", seats=" + seats +
                '}';
    }
}
