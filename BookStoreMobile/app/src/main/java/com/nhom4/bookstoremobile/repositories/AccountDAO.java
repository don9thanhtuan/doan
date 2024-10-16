package com.nhom4.bookstoremobile.repositories;

import android.app.Activity;
import android.database.Cursor;

import com.nhom4.bookstoremobile.entities.Account;

public class AccountDAO {
    private static AccountDAO accountDAO;
    private final AccountTable accountTable;

    public AccountDAO(AccountTable accountTable) {
        this.accountTable = accountTable;
    }

    public static AccountDAO getInstance(Activity activity) {
        if (accountDAO == null) {
            accountDAO = new AccountDAO(new AccountTable(activity));
        }
        return accountDAO;
    }

    public Account getAccountData() {
        Cursor cursor = accountTable.getAccount();
        if (cursor != null && cursor.moveToFirst()) {
            int userIDColumnIndex = cursor.getColumnIndex("userID");
            int isAdminColumnIndex = cursor.getColumnIndex("isAdmin");

            if (userIDColumnIndex != -1 && isAdminColumnIndex != -1) {
                String userID = cursor.getString(userIDColumnIndex);
                int isAdminInt = cursor.getInt(isAdminColumnIndex);

                if (isAdminInt == 0) {
                    return new Account(userID, false);
                } else {
                    return new Account(userID, true);
                }
            }
        }
        return null;
    }

    public void clear() {
        accountTable.clear();
    }

    public void addAccount(String userID, int isAdminInt) {
        accountTable.addAccount(userID, isAdminInt);
    }
}
