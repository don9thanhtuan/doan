package com.nhom4.bookstoremobile.entities;

import java.util.Date;

public class Order {
    private String maDonHang;
    private String idNguoiDat;
    private Date thoiGianDat;
    private String trangThai;
    private String thanhTien;
    private String idSachDau;
    private Book cuonSachDau;
    private int soSanPham;

    public Order() {
    }

    public Order(String MaDonHang, String nguoiDat, Date thoiGianDat, int trangThai, String thanhTien, String cuonSachDau, int soSanPham) {
        this.maDonHang = MaDonHang;
        this.idNguoiDat = nguoiDat;
        this.thoiGianDat = thoiGianDat;
        this.trangThai = convertTrangThaiInt(trangThai);
        this.thanhTien = thanhTien;
        this.idSachDau = cuonSachDau;
        this.soSanPham = soSanPham;
    }

    public Order(String MaDonHang, String nguoiDat, int trangThai, String thanhTien) {
        this.maDonHang = MaDonHang;
        this.idNguoiDat = nguoiDat;
        this.trangThai = convertTrangThaiInt(trangThai);
        this.thanhTien = thanhTien;
    }

    private String convertTrangThaiInt(int trangThai) {
        switch (trangThai) {
            case 0:
                return "Chưa xác nhận";
            case 1:
                return "Đã xác nhận";
            case 2:
                return "Đang vận chuyển";
            case 3:
                return "Đã hoàn thành";
            default:
                return "Đã hủy";
        }
    }

    public int convertTrangThaiString(String trangThai) {
        switch (trangThai) {
            case "Chưa xác nhận":
                return 0;
            case "Đã xác nhận":
                return 1;
            case "Đang vận chuyển":
                return 2;
            case "Đã hoàn thành":
                return 3;
            default:
                return 10;
        }
    }

    public String getMaDonHang() {
        return maDonHang;
    }

    public void setMaDonHang(String maDonHang) {
        this.maDonHang = maDonHang;
    }

    public String getIdNguoiDat() {
        return idNguoiDat;
    }

    public void setIdNguoiDat(String idNguoiDat) {
        this.idNguoiDat = idNguoiDat;
    }

    public Date getThoiGianDat() {
        return thoiGianDat;
    }

    public void setThoiGianDat(Date thoiGianDat) {
        this.thoiGianDat = thoiGianDat;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(int trangThai) {
        this.trangThai = convertTrangThaiInt(trangThai);
    }

    public int getTrangThaiInt() {
        return convertTrangThaiString(trangThai);
    }

    public String getThanhTien() {
        return thanhTien;
    }

    public void setThanhTien(String thanhTien) {
        this.thanhTien = thanhTien;
    }

    public String getIdSachDau() {
        return idSachDau;
    }

    public void setIdSachDau(String idSachDau) {
        this.idSachDau = idSachDau;
    }

    public Book getCuonSachDau() {
        return cuonSachDau;
    }

    public void setCuonSachDau(Book cuonSachDau) {
        this.cuonSachDau = cuonSachDau;
    }

    public int getSoSanPham() {
        return soSanPham;
    }

    public void setSoSanPham(int soSanPham) {
        this.soSanPham = soSanPham;
    }
}