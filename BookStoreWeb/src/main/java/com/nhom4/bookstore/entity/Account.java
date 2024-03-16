package com.nhom4.bookstore.entity;

public class Account {
    private String tenTaiKhoan;
    private String hoTen;
    private String soDienThoai;
    private String email;
    private String diaChi;
    private boolean isAdmin;
    private String matKhau;

    public Account() {}
    public Account(String tenTaiKhoan, String hoTen, String soDienThoai, String email, String diaChi, boolean isAdmin) {
        this.tenTaiKhoan = tenTaiKhoan;
        this.hoTen = hoTen;
        this.soDienThoai = soDienThoai;
        this.email = email;
        this.diaChi = diaChi;
        this.isAdmin = isAdmin;
    }
    
    public Account(String id, String hoTen, String soDienThoai, String email, String diaChi, boolean isAdmin, String matKhau) {
        this.tenTaiKhoan = id;
        this.hoTen = hoTen;
        this.soDienThoai = soDienThoai;
        this.email = email;
        this.diaChi = diaChi;
        this.isAdmin = isAdmin;
        this.matKhau = matKhau;
    }
    public Account(String tenTaiKhoan, String email, String matKhau) {
        this.tenTaiKhoan = tenTaiKhoan;
        this.email = email;
        this.matKhau = matKhau;
    }

    public String getTenTaiKhoan() {
        return tenTaiKhoan;
    }

    public void setTenTaiKhoan(String Id) {
        this.tenTaiKhoan = Id;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String HoTen) {
        this.hoTen = HoTen;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String SoDienThoai) {
        this.soDienThoai = SoDienThoai;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String Email) {
        this.email = Email;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String DiaChi) {
        this.diaChi = DiaChi;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String MatKhau) {
        this.matKhau = MatKhau;
    }
}

