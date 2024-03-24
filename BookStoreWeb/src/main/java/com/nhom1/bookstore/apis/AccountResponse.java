package com.nhom1.bookstore.apis;

public class AccountResponse {
    private String userName;
    private boolean isAdmin;

    public AccountResponse(String userName, boolean isAdmin) {
        this.userName = userName;
        this.isAdmin = isAdmin;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }
}
