package com.example.recruitmenthelper.config;

public class Constant {

    private final static String BASE_CM_URL = "http://10.0.2.2:8080/";
    private final static String BASE_UM_URL = "http://10.0.2.2:8099/";
    //private final static String BASE_URL="http://192.168.1.15:8080/";
    public final static String REGISTER_URL = BASE_UM_URL + "register";
    public final static String LOGIN_URL = BASE_UM_URL + "/users/login";
    public final static String ADD_URL = BASE_CM_URL + "addEvent";
    public final static String GETEVENTS_URL = BASE_CM_URL + "getAllEvents";
    public final static String DELETE_URL=BASE_CM_URL+"deleteEvent";
    public final static String GETEVENT_URL=BASE_CM_URL+"getEvent";
    public final static String UPDATE_URL=BASE_CM_URL+"updateEvent";
    public final static String ATTEND_URL=BASE_CM_URL+"enlistUser";
    public final static String CANCEL_URL=BASE_CM_URL+"cancelAttend";
    public final static String ENLISTED_URL=BASE_CM_URL+"enlistedEvents?username=";
    public final static String GETUSERS_URL=BASE_CM_URL+"getAll";
    public final static String CHECK_URL=BASE_CM_URL+"check";

}
