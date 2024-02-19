package com.postgresql.demo.modal;

public class RegisterPasswordModal {

    private long userId;
    private String username;
    private int newPassword;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(int newPassword) {
        this.newPassword = newPassword;
    }
}
