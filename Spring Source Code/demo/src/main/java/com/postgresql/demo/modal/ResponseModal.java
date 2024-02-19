package com.postgresql.demo.modal;

public class ResponseModal {

    int code;
    String message;
    String object;

    public final static int ADD_USER_SUCCESS = 10001;
    public final static int ADD_USER_FAIL = 10002;
    public final static int UPDATE_USER_SUCCESS = 10003;
    public final static int UPDATE_USER_FAIL = 10004;
    public final static int DELETE_USER_SUCCESS = 10005;
    public final static int DELETE_USER_FAILED = 10006;
    public final static int RETRIEVE_USER_SUCCESS = 10007;
    public final static int RETRIEVE_USER_FAIL = 10008;
    public final static int RETRIEVE_USER_LIST_SUCCESS = 10009;
    public final static int RETRIEVE_USER_LIST_FAIL = 10010;
    public final static int USER_NOT_EXIST = 10011;
    public final static int USER_EXIST = 10012;
    public final static int USERNAME_ALREADY_EXIST = 10013;
    public final static int EMAIL_ALREADY_EXIST = 10014;

    public static int EXCEPTION_ERROR = 500;

    public static String getResponseMsg(int code){
        return switch (code) {
            case ADD_USER_SUCCESS -> "Add User Successfully";
            case ADD_USER_FAIL -> "Add User Failed";
            case UPDATE_USER_SUCCESS -> "Update User Successfully";
            case UPDATE_USER_FAIL -> "Update User Failed";
            case DELETE_USER_SUCCESS -> "Delete User Successfully";
            case DELETE_USER_FAILED -> "Delete User Failed";
            case RETRIEVE_USER_SUCCESS -> "Retrieve User Details Successfully";
            case RETRIEVE_USER_FAIL -> "Retrieve User Details Failed";
            case RETRIEVE_USER_LIST_SUCCESS -> "Retrieve User List Successfully";
            case RETRIEVE_USER_LIST_FAIL -> "Retrieve User List Failed";
            case USER_NOT_EXIST -> "User is not exist";
            case USER_EXIST -> "User is exist";
            case USERNAME_ALREADY_EXIST -> "Failed the username is already exist";
            case EMAIL_ALREADY_EXIST -> "Failed the email is already exist";

            case 500 -> "Exception Error";
            default -> "";
        };
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }
}
