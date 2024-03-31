package com.nhom4.bookstoremobile.sqlite;

import android.app.Activity;
import android.database.Cursor;

import com.nhom4.bookstoremobile.entities.CartItem;

import java.util.ArrayList;
import java.util.List;

public class CartDAO {

    private final CartTable cartTable;
    private static CartDAO cartDAO;

    public CartDAO(CartTable cartTable) {
        this.cartTable = cartTable;
    }

    public static CartDAO getInstance(Activity activity) {
        if (cartDAO == null) {
            cartDAO = new CartDAO(new CartTable(activity));
        }
        return cartDAO;
    }

    public List<CartItem> getCartData() {
        List<CartItem> cart = new ArrayList<>();

        Cursor cursor = cartTable.getAllCartItems();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String bookID = cursor.getString(cursor.getColumnIndex("bookID"));
                int quantity = cursor.getInt(cursor.getColumnIndex("quantity"));
                cart.add(new CartItem(bookID, quantity));
            } while (cursor.moveToNext());
            cursor.close();
        }

        return cart;
    }

    public CartItem getCartItem(String id) {
        Cursor cursor = cartTable.findCartItem(id);

        if (cursor != null && cursor.moveToFirst()) {
            int quantity = cursor.getInt(cursor.getColumnIndex("quantity"));
            return new CartItem(id, quantity);
        }
        return null;
    }
}
