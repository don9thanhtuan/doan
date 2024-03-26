package com.nhom4.bookstoremobile.entities;

public class AccountResponse {
    private String userID;
    private boolean admin;

    public AccountResponse(String userID, boolean admin) {
        this.userID = userID;
        this.admin = admin;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }
}
