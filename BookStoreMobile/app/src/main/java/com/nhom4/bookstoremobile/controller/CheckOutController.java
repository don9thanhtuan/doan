package com.nhom4.bookstoremobile.controller;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.nhom4.bookstoremobile.R;
import com.nhom4.bookstoremobile.activity.ViewAccount;
import com.nhom4.bookstoremobile.activity.ViewCart;
import com.nhom4.bookstoremobile.adapter.OrderItemAdapter;
import com.nhom4.bookstoremobile.entities.Account;
import com.nhom4.bookstoremobile.entities.Book;
import com.nhom4.bookstoremobile.entities.CartItem;
import com.nhom4.bookstoremobile.entities.OrderDTO;
import com.nhom4.bookstoremobile.retrofit.DefaultURL;
import com.nhom4.bookstoremobile.retrofit.RetrofitAPI;
import com.nhom4.bookstoremobile.service.BookService;
import com.nhom4.bookstoremobile.service.OrderService;
import com.nhom4.bookstoremobile.sqlite.CartTable;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckOutController {
    private final List<TextView> btnList = new ArrayList<>();
    private final Activity activity;
    private OrderItemAdapter adapter;
    private List<CartItem> orderItemList;
    private String paymentMethod;


    public CheckOutController(Activity activity) {
        this.activity = activity;
    }

    public Activity getActivity() {
        return activity;
    }

    private List<Boolean> convertArrayToList(boolean[] booleanArray) {
        List<Boolean> booleanList = new ArrayList<>();
        for (boolean b : booleanArray) {
            booleanList.add(b);
        }
        return booleanList;
    }

    public void addBtnToList(TextView btn) {
        btnList.add(btn);
    }

    public void setEffect(TextView clickedBtn) {
        if (clickedBtn.getText().equals("Thanh toán khi nhận hàng")) {
            paymentMethod = "cod";
        } else {
            paymentMethod = "online";
        }

        for (TextView button : btnList) {
            if (button == clickedBtn) {
                button.setBackgroundResource(R.drawable.selection_border_rounded);
                continue;
            }
            button.setBackgroundResource(R.drawable.gray_border_rounded);
        }
    }

    public List<CartItem> getCartData() {
        List<CartItem> cart = new ArrayList<>();

        CartTable cartTable = new CartTable(activity);
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

    public void getOrderItemList(boolean[] checkedArray) {
        List<Boolean> checkedList = convertArrayToList(checkedArray);
        orderItemList = getCartData();

        if (orderItemList.size() != 0) {
            for (int i = 0; i < orderItemList.size(); i++) {
                if (checkedList.get(i)) {
                    getBookDetailFromAPI(orderItemList.get(i));
                } else {
                    checkedList.remove(i);
                    orderItemList.remove(i);
                    i--;
                }
            }
        }

        RecyclerView recyclerView = activity.findViewById(R.id.orderItemList);

        adapter = new OrderItemAdapter(activity, orderItemList, this);

        recyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
    }

    public void buyNow(CartItem cartItem) {
        getBookDetailFromAPI(cartItem);

        orderItemList = new ArrayList<>();
        orderItemList.add(cartItem);

        RecyclerView recyclerView = activity.findViewById(R.id.orderItemList);

        adapter = new OrderItemAdapter(activity, orderItemList, this);

        recyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
    }

    private void getBookDetailFromAPI(CartItem cartItem) {
        BookService bookService = RetrofitAPI.getInstance().create(BookService.class);
        Call<Book> call = bookService.getBookDetailsFromRestAPI(cartItem.getBookID());
        call.enqueue(new Callback<Book>() {
            @Override
            public void onResponse(Call<Book> call, Response<Book> response) {
                if (response.isSuccessful()) {
                    Book book = response.body();
                    String imageURL = DefaultURL.getUrl() + book.getHinhAnh();
                    book.setHinhAnh(imageURL);
                    cartItem.setBook(book);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<Book> call, Throwable t) {
            }
        });
    }

    public void createOrder(Account account) {
        String phone = account.getSoDienThoai();
        String address = account.getDiaChi();
        if (phone == null || phone.trim().isEmpty()) {
            Toast.makeText(activity, "Vui lòng kiểm tra lại số điện thoại", Toast.LENGTH_SHORT).show();
            return;
        } else if (address == null || address.trim().isEmpty()) {
            Toast.makeText(activity, "Vui lòng kiểm tra lại địa chỉ", Toast.LENGTH_SHORT).show();
            return;
        }

        OrderDTO orderDTO = new OrderDTO();
        List<String> bookList = new ArrayList<>();
        List<String> quantityList = new ArrayList<>();

        for (CartItem item : orderItemList) {
            bookList.add(item.getBookID());
            quantityList.add(String.valueOf(item.getQuantity()));
        }

        orderDTO.setBookList(bookList);
        orderDTO.setQuantityList(quantityList);
        orderDTO.setPrice(calculatePrice());
        orderDTO.setPhone(phone);
        orderDTO.setAddress(address);
        orderDTO.setPaymentMethod(paymentMethod);

        OrderService orderService = RetrofitAPI.getInstance().create(OrderService.class);
        Call<String> call = orderService.createOrder(account.getTenTaiKhoan(), orderDTO);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    afterOrder();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                afterOrder();
            }
        });
    }

    private void afterOrder() {
        CartTable cartTable = new CartTable(activity);
        for (CartItem item : orderItemList) {
            cartTable.removeFromCart(item.getBookID());
        }

        Toast.makeText(activity, "Đặt hàng thành công", Toast.LENGTH_SHORT).show();
        redirectToAccount();
    }

    private String calculatePrice() {
        int totalPrice = 0;
        for (CartItem item : orderItemList) {
            Book book = item.getBook();

            if (book != null) {
                String priceRaw = book.getGia();
                priceRaw = priceRaw.replace("₫", "");
                priceRaw = priceRaw.replaceAll("\\s+", "");
                priceRaw = priceRaw.replace(".", "");

                int price = Integer.parseInt(priceRaw);
                totalPrice += (item.getQuantity() * price);
            }
        }
        totalPrice += 20000;
        return String.format("%,d", totalPrice).replace(',', '.') + " ₫";
    }

    public void redirectToAccount() {
        Intent intent = new Intent(activity, ViewAccount.class);
        activity.startActivity(intent);
        activity.finish();
    }

    public void redirectToCart() {
        Intent intent = new Intent(activity, ViewCart.class);
        activity.startActivity(intent);
        activity.finish();
    }

    public void reload(SwipeRefreshLayout pullToRefresh) {
        activity.recreate();
        pullToRefresh.setRefreshing(false);
    }
}
