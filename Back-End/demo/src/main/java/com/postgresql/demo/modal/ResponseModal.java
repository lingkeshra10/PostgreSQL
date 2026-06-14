package com.postgresql.demo.modal;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseModal {
    private int code;
    private String message;
    private Object data;

    public static final int ADD_USER_SUCCESS = 10001;
    public static final int ADD_USER_FAIL = 10002;
    public static final int UPDATE_USER_SUCCESS = 10003;
    public static final int UPDATE_USER_FAIL = 10004;
    public static final int DELETE_USER_SUCCESS = 10005;
    public static final int DELETE_USER_FAILED = 10006;
    public static final int RETRIEVE_USER_SUCCESS = 10007;
    public static final int RETRIEVE_USER_FAIL = 10008;
    public static final int RETRIEVE_USER_LIST_SUCCESS = 10009;
    public static final int RETRIEVE_USER_LIST_FAIL = 10010;
    public static final int USER_NOT_EXIST = 10011;
    public static final int USER_EXIST = 10012;
    public static final int USERNAME_ALREADY_EXIST = 10013;
    public static final int EMAIL_ALREADY_EXIST = 10014;

    public static final int EXCEPTION_ERROR = 500;

    public static String getResponseMsg(int code) {
        return switch (code) {
            case ADD_USER_SUCCESS -> "User added successfully";
            case ADD_USER_FAIL -> "Failed to add user";
            case UPDATE_USER_SUCCESS -> "User updated successfully";
            case UPDATE_USER_FAIL -> "Failed to update user";
            case DELETE_USER_SUCCESS -> "User deleted successfully";
            case DELETE_USER_FAILED -> "Failed to delete user";
            case RETRIEVE_USER_SUCCESS -> "User details retrieved successfully";
            case RETRIEVE_USER_FAIL -> "Failed to retrieve user details";
            case RETRIEVE_USER_LIST_SUCCESS -> "User list retrieved successfully";
            case RETRIEVE_USER_LIST_FAIL -> "Failed to retrieve user list";
            case USER_NOT_EXIST -> "User does not exist";
            case USER_EXIST -> "User exists";
            case USERNAME_ALREADY_EXIST -> "Username already exists";
            case EMAIL_ALREADY_EXIST -> "Email already exists";
            case EXCEPTION_ERROR -> "An unexpected error occurred";
            default -> "Unknown response code";
        };
    }
}