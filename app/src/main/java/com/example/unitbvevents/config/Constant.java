package com.example.unitbvevents.config;

public class Constant {

    private final static String BASE_URL = "http://10.0.2.2:8080/";
    //private final static String BASE_URL="http://192.168.1.15:8080/";
    public final static String REGISTER_URL = BASE_URL + "register";
    public final static String LOGIN_URL = BASE_URL + "login";
    public final static String ADD_URL = BASE_URL + "addEvent";
    public final static String GETEVENTS_URL = BASE_URL + "getAllEvents";
    public final static String DELETE_URL=BASE_URL+"deleteEvent";
    public final static String GETEVENT_URL=BASE_URL+"getEvent";
    public final static String UPDATE_URL=BASE_URL+"updateEvent";
    public final static String ATTEND_URL=BASE_URL+"enlistUser";
    public final static String ENLISTED_URL=BASE_URL+"enlistedEvents?username=";

}
