package com.nhom4.bookstore.DTO;

import java.util.List;

public class OrderDTO {
    private List<String> bookList;
    private List<String> priceList;
    private String price;
    private String phone;
    private String address;
    private String paymentMethod;
    
    public List<String> getBookList() {
        return bookList;
    }
    public void setBookList(List<String> bookList) {
        this.bookList = bookList;
    }
    public List<String> getPriceList() {
        return priceList;
    }
    public void setPriceList(List<String> priceList) {
        this.priceList = priceList;
    }
    public String getPrice() {
        return price;
    }
    public void setPrice(String price) {
        this.price = price;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getPaymentMethod() {
        return paymentMethod;
    }
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    
}



