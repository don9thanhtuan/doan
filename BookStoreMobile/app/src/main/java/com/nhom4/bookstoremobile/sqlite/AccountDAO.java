package com.nhom4.bookstoremobile.sqlite;

import android.app.Activity;
import android.database.Cursor;

import com.nhom4.bookstoremobile.entities.AccountResponse;

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

    public AccountResponse getAccountData() {
        Cursor cursor = accountTable.getAccount();
        if (cursor != null && cursor.moveToFirst()) {
            String userID = cursor.getString(cursor.getColumnIndex("userID"));
            int isAdminInt = cursor.getInt(cursor.getColumnIndex("isAdmin"));

            if (isAdminInt == 0) {
                return new AccountResponse(userID, false);
            } else {
                return new AccountResponse(userID, true);
            }
        }
        return null;
    }
}
