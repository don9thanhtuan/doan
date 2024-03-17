package com.nhom1.bookstore.entity;

import java.util.List;

public class OrderDetail {
    private String maDonHang;
    private String soDienThoai;
    private String diaChi;
    private List<BookInOrder> bookList;

    public OrderDetail(){}
    public OrderDetail(String maDonHang, List<BookInOrder> bookList) {
        this.maDonHang = maDonHang;
        this.bookList = bookList;
    }

    public class BookInOrder{ 
        private String idSach;
        private Book sach;
        private int soLuong;
        
        public BookInOrder(String idSach, int soluong) {
            this.idSach = idSach;
            this.soLuong = soluong;
        }
        public String getIdSach() {
            return idSach;
        }
        public void setIdSach(String idSach) {
            this.idSach = idSach;
        }
        public Book getSach() {
            return sach;
        }
        public void setSach(Book sach) {
            this.sach = sach;
        }
        public int getSoLuong() {
            return soLuong;
        }
        public void setSoLuong(int soluong) {
            this.soLuong = soluong;
        }
    }

    public String getMaDonHang() {
        return maDonHang;
    }

    public void setMaDonHang(String maDonHang) {
        this.maDonHang = maDonHang;
    }

    public List<BookInOrder> getBookList() {
        return bookList;
    }

    public void setBookList(List<BookInOrder> bookList) {
        this.bookList = bookList;
    }
    public String getSoDienThoai() {
        return soDienThoai;
    }
    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }
    public String getDiaChi() {
        return diaChi;
    }
    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }
    
}
