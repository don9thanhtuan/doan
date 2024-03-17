package com.nhom1.bookstore.entity;

public class Book {
    private String id;
    private String ten;
    private String hinhAnh;
    private String tacGia;
    private String nhaCungCap;
    private int tonKho;
    private int daBan;
    private String gia;
    private double trongLuong;
    private String kichThuoc;
    private String gioiThieu;

    public Book() {}
    public Book(String id, String ten, String hinhAnh, String tacGia, String nhaCungCap, int tonKho, String gia,
            int daBan, double trongLuong, String kichThuoc, String gioiThieu) {
        this.id = id;
        this.ten = ten;
        this.hinhAnh = hinhAnh;
        this.tacGia = tacGia;   
        this.nhaCungCap = nhaCungCap;
        this.tonKho = tonKho;
        this.gia = gia;
        this.daBan = daBan;
        this.trongLuong = trongLuong;
        this.kichThuoc = kichThuoc;
        this.gioiThieu = gioiThieu;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getTen() {
        return ten;
    }
    public void setTen(String ten) {
        this.ten = ten;
    }
    public String getHinhAnh() {
        return hinhAnh;
    }
    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }
    public String getTacGia() {
        return tacGia;
    }
    public void setTacGia(String tacGia) {
        this.tacGia = tacGia;
    }
    public String getNhaCungCap() {
        return nhaCungCap;
    }
    public void setNhaCungCap(String nhaCungCap) {
        this.nhaCungCap = nhaCungCap;
    }
    public int getTonKho() {
        return tonKho;
    }
    public void setTonKho(int tonKho) {
        this.tonKho = tonKho;
    }
    public String getGia() {
        return gia;
    }
    public void setGia(String gia) {
        this.gia = gia;
    }
    public int getDaBan() {
        return daBan;
    }
    public void setDaBan(int daBan) {
        this.daBan = daBan;
    }
    public double getTrongLuong() {
        return trongLuong;
    }
    public void setTrongLuong(double trongLuong) {
        this.trongLuong = trongLuong;
    }
    public String getKichThuoc() {
        return kichThuoc;
    }
    public void setKichThuoc(String kichThuoc) {
        this.kichThuoc = kichThuoc;
    }
    public String getGioiThieu() {
        return gioiThieu;
    }
    public void setGioiThieu(String gioiThieu) {
        this.gioiThieu = gioiThieu;
    }
}
