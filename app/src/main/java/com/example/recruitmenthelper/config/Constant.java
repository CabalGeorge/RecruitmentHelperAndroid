package com.example.recruitmenthelper.config;

public class Constant {

    private final static String BASE_CM_URL = "http://10.0.2.2:8080/";
    private final static String BASE_UM_URL = "http://10.0.2.2:8099/";
    //private final static String BASE_URL="http://192.168.1.15:8080/";
    public final static String LOGIN_URL = BASE_UM_URL + "/users/login";
    public final static String CREATE_USER_URL = BASE_UM_URL + "/users";
    public final static String GET_ALL_USERS_URL = BASE_UM_URL + "/users";
    public final static String GET_ACTIVE_CANDIDATES_URL = BASE_CM_URL + "/candidates/unarchived";
    public final static String GET_ARCHIVED_CANDIDATES_URL = BASE_CM_URL + "/candidates/archived";
    public final static String GET_USER_BY_EMAIL_URL = BASE_UM_URL + "users/getByEmail";
    public final static String UPDATE_USER_URL = BASE_UM_URL + "/users/updateUser";
    public final static String DELETE_USER_URL = BASE_UM_URL + "/users";
    public final static String GET_CANDIDATE_BY_ID = BASE_CM_URL + "/candidates";
    public final static String DELETE_CANDIDATE_URL = BASE_CM_URL + "/candidates";
    public final static String ARCHIVE_CANDIDATE_URL = BASE_CM_URL + "/candidates";
    public final static String GETEVENT_URL = BASE_CM_URL + "/candidates";
    public final static String DELETE_URL = BASE_CM_URL + "/candidates";
    public final static String GETEVENTS_URL = BASE_CM_URL + "/candidates";
}
