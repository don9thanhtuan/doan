package com.nhom1.bookstore.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.nhom1.bookstore.entity.Account;

@Repository
public class AccountDAOImpl implements AccountDAO{
    private Connection conn;

    public AccountDAOImpl() {
        this.conn = JDBC.getConnection();
        if(conn != null) {System.out.println("Account connect success");}
    }

    @Override
    public void deleteAccount(String id) {
        String sql = "Delete from TaiKhoan WHERE TenTaiKhoan = ?";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, id);

            statement.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void editAccount(String currentID, Account newAccount) {
        String sql = "UPDATE TaiKhoan SET HoTen=?, SoDienThoai=?, DiaChi=? WHERE TenTaiKhoan = ?";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, newAccount.getHoTen());
            statement.setString(2, newAccount.getSoDienThoai());
            statement.setString(3, newAccount.getDiaChi());
            statement.setString(4, currentID);

            statement.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    

    @Override
    public void changePassword(String currentID, String newPassword) {
        String sql = "UPDATE TaiKhoan SET MatKhau=? WHERE TenTaiKhoan = ?";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, newPassword);
            statement.setString(2, currentID);

            statement.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Account getAccount(String userName) {
        String sql = "SELECT * FROM TaiKhoan where TenTaiKhoan = ?";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, userName);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String tenTaiKhoan = resultSet.getString("TenTaiKhoan");
                    String hoTen = resultSet.getString("HoTen");
                    String soDienThoai = resultSet.getString("SoDienThoai");
                    String email = resultSet.getString("Email");
                    String diaChi = resultSet.getString("DiaChi");
                    boolean isAdmin = resultSet.getBoolean("isAdmin");
                    String matKhau = resultSet.getString("MatKhau");
                    return new Account(tenTaiKhoan, hoTen, soDienThoai, email, diaChi, isAdmin, matKhau);
                }
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Account> getAccountList() {
        List<Account> accountList = new ArrayList<>();
        String sql = "SELECT * FROM TaiKhoan";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String tenTaiKhoan = resultSet.getString("TenTaiKhoan");
                    String hoTen = resultSet.getString("HoTen");
                    String soDienThoai = resultSet.getString("SoDienThoai");
                    String email = resultSet.getString("Email");
                    String diaChi = resultSet.getString("DiaChi");
                    boolean isAdmin = resultSet.getBoolean("isAdmin");
                    accountList.add(new Account(tenTaiKhoan, hoTen, soDienThoai, email, diaChi, isAdmin));
                }
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return accountList;
    }

    @Override
    public List<Account> search(String tuKhoa) {
        List<Account> result = new ArrayList<>();
        String sql = "SELECT * FROM TaiKhoan WHERE LOWER(TenTaiKhoan) LIKE LOWER(?) OR LOWER(HoTen) LIKE LOWER(?) OR LOWER(SoDienThoai) LIKE LOWER(?) OR LOWER(Email) LIKE LOWER(?) OR LOWER(DiaChi) LIKE LOWER(?)";
        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, "%" + tuKhoa + "%");
            preparedStatement.setString(2, "%" + tuKhoa + "%");
            preparedStatement.setString(3, "%" + tuKhoa + "%");
            preparedStatement.setString(4, "%" + tuKhoa + "%");
            preparedStatement.setString(5, "%" + tuKhoa + "%");

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Account account = new Account();
                    account.setTenTaiKhoan(resultSet.getString("TenTaiKhoan"));
                    account.setHoTen(resultSet.getString("HoTen"));
                    account.setSoDienThoai(resultSet.getString("SoDienThoai"));
                    account.setEmail(resultSet.getString("Email"));
                    account.setDiaChi(resultSet.getString("DiaChi"));
                    result.add(account);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void registerAccount(Account newAccount) {
        String sql = "INSERT INTO TaiKhoan (TenTaiKhoan, MatKhau, Email, isAdmin) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, newAccount.getTenTaiKhoan());
            preparedStatement.setString(2, newAccount.getMatKhau());
            preparedStatement.setString(3, newAccount.getEmail());
            preparedStatement.setBoolean(4, false);

            preparedStatement.execute();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    
}
