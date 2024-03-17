package com.nhom1.bookstore.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.nhom1.bookstore.entity.Order;
import com.nhom1.bookstore.entity.OrderDetail;
import com.nhom1.bookstore.entity.OrderDetail.BookInOrder;
import com.nhom1.bookstore.services.ConverterCurrency;

@Repository
public class OrderDAOImpl implements OrderDAO{
    private Connection conn;

    public OrderDAOImpl() {
        this.conn = JDBC.getConnection();
        if(conn != null) {System.out.println("Order connect success");}
    }

    @Override
    public void editStatusOrder(String currentID, int newStatus) {
        String sql = "UPDATE DonHang SET TrangThai = ? WHERE MaDonHang = ?;";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, newStatus);
            statement.setString(2, currentID);

            statement.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Order getOrder(String id) {
        String sql = "SELECT * FROM DonHang where MaDonHang = ?";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String maDonHang = resultSet.getString("MaDonHang");
                    String idNguoiDat = resultSet.getString("IDNguoiDat");
                    int trangThai = resultSet.getInt("TrangThai");
                    int thanhTienRaw = resultSet.getInt("ThanhTien");
                    String thanhTien = ConverterCurrency.numberToCurrency(thanhTienRaw);

                    return new Order(maDonHang, idNguoiDat, trangThai, thanhTien);
                }
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public OrderDetail getOrderDetail(String id) {
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setMaDonHang(id);
        orderDetail.setBookList(new ArrayList<>());
        String sql = "SELECT * FROM ChiTietDonHang where MaDonHang = ?";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String idSach = resultSet.getString("IDSach");
                    int soLuong = resultSet.getInt("SoLuong");
                    String soDienThoai = resultSet.getString("SoDienThoai");
                    orderDetail.setSoDienThoai(soDienThoai);
                    String diaChi = resultSet.getString("DiaChi");
                    orderDetail.setDiaChi(diaChi);
                    OrderDetail.BookInOrder bookInOrder = orderDetail.new BookInOrder(idSach, soLuong);
                    orderDetail.getBookList().add(bookInOrder);
                }
                return orderDetail;
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Order> getOrderList() {
        List<Order> orderList = new ArrayList<>();
        String sql = "SELECT * FROM DonHang";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String maDonHang = resultSet.getString("MaDonHang");
                    String idNguoiDat = resultSet.getString("IDNguoiDat");
                    Date thoiGianDat = resultSet.getTimestamp("ThoiGianDat");
                    int trangThai = resultSet.getInt("TrangThai");
                    int thanhTienRaw = resultSet.getInt("ThanhTien");
                    String thanhTien = ConverterCurrency.numberToCurrency(thanhTienRaw);
                    String idSachDau = resultSet.getString("IDSachDau");
                    int soSanPham = resultSet.getInt("SoSanPham");
                    orderList.add(new Order(maDonHang, idNguoiDat, thoiGianDat, trangThai, thanhTien, idSachDau, soSanPham));
                }
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return orderList;
    }

    @Override
    public List<Order> search(String tuKhoa) {
        List<Order> result = new ArrayList<>();
        String sql = "SELECT * FROM DonHang WHERE LOWER(MaDonHang) LIKE LOWER(?) OR LOWER(IDNguoiDat) LIKE LOWER(?) OR LOWER(ThoiGianDat) LIKE LOWER(?) OR LOWER(IDSachDau) LIKE LOWER(?) OR LOWER(SoSanPham) LIKE LOWER(?)";
        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, "%" + tuKhoa + "%");
            preparedStatement.setString(2, "%" + tuKhoa + "%");
            preparedStatement.setString(3, "%" + tuKhoa + "%");
            preparedStatement.setString(4, "%" + tuKhoa + "%");
            preparedStatement.setString(5, "%" + tuKhoa + "%");

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String maDonHang = resultSet.getString("MaDonHang");
                    String idNguoiDat = resultSet.getString("IDNguoiDat");
                    Date thoiGianDat = resultSet.getTimestamp("ThoiGianDat");
                    int trangThai = resultSet.getInt("TrangThai");

                    int thanhTienRaw = resultSet.getInt("ThanhTien");
                    String thanhTien = ConverterCurrency.numberToCurrency(thanhTienRaw);

                    String idSachDau = resultSet.getString("IDSachDau");
                    int soSanPham = resultSet.getInt("SoSanPham");
                    result.add(new Order(maDonHang, idNguoiDat, thoiGianDat, trangThai, thanhTien, idSachDau, soSanPham));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void createOrder(Order newOrder) {
        String sql = "INSERT INTO DonHang (MaDonHang, IDNguoiDat, ThoiGianDat, TrangThai, ThanhTien, IDSachDau, SoSanPham) " +
                         "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, newOrder.getMaDonHang());
            statement.setString(2, newOrder.getIdNguoiDat());
            statement.setObject(3, newOrder.getThoiGianDat());
            statement.setInt(4, newOrder.convertTrangThaiString(newOrder.getTrangThai()));

            int thanhTien = ConverterCurrency.currencyToNumber(newOrder.getThanhTien());
            statement.setInt(5, thanhTien);
            statement.setString(6, newOrder.getIdSachDau());
            statement.setInt(7, newOrder.getSoSanPham());

            statement.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createOrderDetail(OrderDetail newOrderDetail) {
        String sql = "INSERT INTO chitietdonhang (MaDonHang, SoDienThoai, DiaChi, IDSach, SoLuong) VALUES (?, ?, ?, ?, ?)";
        List<OrderDetail.BookInOrder> bookList =  newOrderDetail.getBookList();

        for (BookInOrder bookInOrder : bookList) {
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setString(1, newOrderDetail.getMaDonHang());
                statement.setString(2, newOrderDetail.getSoDienThoai());
                statement.setString(3, newOrderDetail.getDiaChi());

                statement.setString(4, bookInOrder.getIdSach());
                statement.setInt(5, bookInOrder.getSoLuong());
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}