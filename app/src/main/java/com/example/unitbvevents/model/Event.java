package com.example.unitbvevents.model;

import java.time.LocalDateTime;

public class Event {

    private String name;
    private String dateTime;
    private String location;
    private User createdBy;


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

    public Event(String name, String dateTime, String location){
        this.name=name;
        this.dateTime=dateTime;
        this.location=location;
    }

    public Event(){}

    @Override
    public String toString() {
        return "Event{" +
                "name='" + name + '\'' +
                ", time=" + dateTime +
                ", location='" + location + '\'' +
                '}';
    }
}
