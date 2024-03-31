package com.nhom4.bookstoremobile.sqlite;

import android.app.Activity;
import android.database.Cursor;

import com.google.gson.GsonBuilder;
import com.nhom4.bookstoremobile.entities.AccountResponse;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AccountDAO {
    private final AccountTable accountTable;
    private static AccountDAO accountDAO;

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
