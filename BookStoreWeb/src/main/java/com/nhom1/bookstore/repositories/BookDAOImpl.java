package com.nhom1.bookstore.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.nhom1.bookstore.entity.Book;
import com.nhom1.bookstore.services.ConverterCurrency;

@Repository
public class BookDAOImpl implements BookDAO{
    private Connection conn;

    public BookDAOImpl() {
        this.conn = JDBC.getConnection();
        if(conn != null) {System.out.println("Book connect success");}
    }

    @Override
    public void addBook(Book newBook) {
        String sql =
            "INSERT INTO Sach (ID, Ten, HinhAnh, TacGia, NhaXuatBan, Gia, DaBan, TonKho, TrongLuong, KichThuoc, GioiThieu) " +
            "VALUES (?, ?, ?, ?, ?, ?, 0, ?, ?, ?, ?)";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, newBook.getId());
            statement.setString(2, newBook.getTen());
            statement.setString(3, newBook.getHinhAnh());
            statement.setString(4, newBook.getTacGia());
            statement.setString(5, newBook.getNhaCungCap());
            
            int gia = ConverterCurrency.currencyToNumber(newBook.getGia());
            statement.setInt(6, gia);

            statement.setInt(7, newBook.getTonKho());
            statement.setDouble(8, newBook.getTrongLuong());
            statement.setString(9, newBook.getKichThuoc());
            statement.setString(10, newBook.getGioiThieu());

            statement.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteBook(String id) {
        String sql = "Delete from Sach WHERE ID = ?";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, id);

            statement.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void editBook(Book newBook) {
        String sql = 
            "UPDATE Sach SET Ten = ?, HinhAnh = ?, TacGia = ?, NhaXuatBan = ?, Gia = ?, TonKho = ?, "+
            "TrongLuong = ?, KichThuoc = ?, GioiThieu = ? WHERE ID = ?";

        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, newBook.getTen());
            statement.setString(2, newBook.getHinhAnh());
            statement.setString(3, newBook.getTacGia());
            statement.setString(4, newBook.getNhaCungCap());
            
            int gia = ConverterCurrency.currencyToNumber(newBook.getGia());
            statement.setInt(5, gia);

            statement.setInt(6, newBook.getTonKho());
            statement.setDouble(7, newBook.getTrongLuong());
            statement.setString(8, newBook.getKichThuoc());
            statement.setString(9, newBook.getGioiThieu());
            statement.setString(10, newBook.getId());

            statement.executeUpdate();
        }catch (SQLException e) {
                e.printStackTrace();
        }
    }
    
    @Override
    public void updateSoldQuantity(String id, int daBan) {
        String sql = "UPDATE Sach SET DaBan = ? WHERE ID = ?";

        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, daBan);
            statement.setString(2, id);

            statement.executeUpdate();
        }catch (SQLException e) {
                e.printStackTrace();
        }
    }

    @Override
    public Book getBook(String id) {
        String sql = "SELECT * FROM Sach where id = ?";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String ten = resultSet.getString("Ten");
                    String hinhAnh = resultSet.getString("HinhAnh");
                    String tacGia = resultSet.getString("TacGia");
                    String nhaXuatBan = resultSet.getString("NhaXuatBan");
                    int tonKho = resultSet.getInt("TonKho");
                    
                    int giaRaw = resultSet.getInt("Gia");
                    String gia = ConverterCurrency.numberToCurrency(giaRaw);
                    
                    int daBan = resultSet.getInt("DaBan");
                    double trongLuong = resultSet.getDouble("TrongLuong");
                    String kichThuoc = resultSet.getString("KichThuoc");
                    String gioiThieu = resultSet.getString("GioiThieu");

                     return new Book(id, ten, hinhAnh, tacGia, nhaXuatBan, tonKho, gia, daBan, trongLuong, kichThuoc, gioiThieu);
                }
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Book> getBookList() {
        List<Book> bookList = new ArrayList<>();
        String sql = "SELECT * FROM Sach";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String id = resultSet.getString("ID");
                    String ten = resultSet.getString("Ten");
                    String hinhAnh = resultSet.getString("HinhAnh");
                    String tacGia = resultSet.getString("TacGia");
                    String nhaXuatBan = resultSet.getString("NhaXuatBan");
                    int tonKho = resultSet.getInt("TonKho");

                    int giaRaw = resultSet.getInt("Gia");
                    String gia = ConverterCurrency.numberToCurrency(giaRaw);
                    
                    int daBan = resultSet.getInt("DaBan");
                    double trongLuong = resultSet.getDouble("TrongLuong");
                    String kichThuoc = resultSet.getString("KichThuoc");
                    String gioiThieu = resultSet.getString("GioiThieu");

                    bookList.add(new Book(id, ten, hinhAnh, tacGia, nhaXuatBan, tonKho, gia, daBan, trongLuong, kichThuoc, gioiThieu));
                }
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return bookList;
    }

    @Override
    public List<Book> search(String tuKhoa) {
        List<Book> result = new ArrayList<>();
        String sql = "SELECT * FROM TaiKhoan WHERE LOWER(ID) LIKE LOWER(?) OR LOWER(Ten) LIKE LOWER(?) OR LOWER(TacGia) LIKE LOWER(?) OR LOWER(NhaXuatBan) LIKE LOWER(?) OR LOWER(Gia) LIKE LOWER(?) OR LOWER(GioiThieu) LIKE LOWER(?)";
        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, "%" + tuKhoa + "%");
            preparedStatement.setString(2, "%" + tuKhoa + "%");
            preparedStatement.setString(3, "%" + tuKhoa + "%");
            preparedStatement.setString(4, "%" + tuKhoa + "%");
            preparedStatement.setString(5, "%" + tuKhoa + "%");
            preparedStatement.setString(6, "%" + tuKhoa + "%");

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String ten = resultSet.getString("Ten");
                    String hinhAnh = resultSet.getString("HinhAnh");
                    String tacGia = resultSet.getString("TacGia");
                    String nhaXuatBan = resultSet.getString("NhaXuatBan");
                    int tonKho = resultSet.getInt("TonKho");
                    
                    int giaRaw = resultSet.getInt("Gia");
                    String gia = ConverterCurrency.numberToCurrency(giaRaw);
                    
                    int daBan = resultSet.getInt("DaBan");
                    double trongLuong = resultSet.getDouble("TrongLuong");
                    String kichThuoc = resultSet.getString("KichThuoc");
                    String gioiThieu = resultSet.getString("GioiThieu");
                    result.add(new Book(sql, ten, hinhAnh, tacGia, nhaXuatBan, tonKho, gia, daBan, trongLuong, kichThuoc, gioiThieu));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

}
