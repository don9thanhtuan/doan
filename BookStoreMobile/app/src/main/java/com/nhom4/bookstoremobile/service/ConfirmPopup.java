package com.nhom4.bookstoremobile.service;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class ConfirmPopup {
    public static void show(Context context, String title, String message, DialogInterface.OnClickListener positiveClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", positiveClickListener)
                .setNegativeButton("Cancel", null)
                .show();
    }


}
