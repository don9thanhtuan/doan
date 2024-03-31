package com.nhom4.bookstoremobile.entities;

public class OrderResponse {
    private Order order;
    private String userName;

    public OrderResponse(Order order, String userName) {
        this.order = order;
        this.userName = userName;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
