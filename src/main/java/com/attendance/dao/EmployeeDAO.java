package com.attendance.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.attendance.model.Employee;

public class EmployeeDAO {

    // 環境に合わせて書き換えてください
    private static final String URL      = "jdbc:mysql://192.168.0.154:3306/demoDB?useSSL=false&serverTimezone=UTC";
    private static final String USER     = "remote_user";
    private static final String PASSWORD = "password";

    // 既存のfindByIdメソッド
    public Employee findById(String id) {
        Employee emp = null;
        String sql = "SELECT employee_id, name FROM employees WHERE employee_id = ?";

        try {
            // ドライバ読み込み（1回だけで OK な場合もあります）
            Class.forName("com.mysql.cj.jdbc.Driver");

            try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                 PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setString(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        emp = new Employee();
                        emp.setId(rs.getString("employee_id"));
                        emp.setName(rs.getString("name"));
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("JDBCドライバが見つかりません", e);
        } catch (SQLException e) {
            throw new RuntimeException("社員情報の取得に失敗しました", e);
        }
        return emp;
    }

    // 新しく追加したfindEmployeeNameByIdメソッド
    public String findEmployeeNameById(int employeeId) {
        String employeeName = null;
        String sql = "SELECT name FROM employees WHERE employee_id = ?";

        try {
            // ドライバ読み込み
            Class.forName("com.mysql.cj.jdbc.Driver");

            try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                 PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setInt(1, employeeId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        employeeName = rs.getString("name");
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("JDBCドライバが見つかりません", e);
        } catch (SQLException e) {
            throw new RuntimeException("社員情報の取得に失敗しました", e);
        }
        return employeeName;
    }
}
