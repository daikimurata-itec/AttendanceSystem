package com.attendance;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/staffDeleteSearch")
public class StaffDeleteSearch extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final String DB_URL = "jdbc:mysql://192.168.0.154:3306/demoDB?serverTimezone=UTC";
    private static final String DB_USER = "remote_user";
    private static final String DB_PASS = "password";

    // GETメソッドに対応（主処理ここ）
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String empIdStr = request.getParameter("employeeId");
        Integer empId = null;
        String name = null;

        if (empIdStr == null || empIdStr.trim().isEmpty()) {
            request.setAttribute("searched", "emptyId");
            request.getRequestDispatcher("/WEB-INF/view/staffDeleteSearch.jsp").forward(request, response);
            return;
        }

        try {
            empId = Integer.parseInt(empIdStr);

            Class.forName("com.mysql.cj.jdbc.Driver");

            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
                 PreparedStatement stmt = conn.prepareStatement("SELECT name FROM employees WHERE employee_id = ?")) {

                stmt.setInt(1, empId);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    name = rs.getString("name");
                    request.setAttribute("id", empId);
                    request.setAttribute("name", name);
                    request.setAttribute("searched", "found");
                } else {
                    request.setAttribute("searched", "notFound");
                }

            }

        } catch (NumberFormatException e) {
            request.setAttribute("searched", "invalidId");
        } catch (ClassNotFoundException | SQLException e) {
            throw new ServletException(e);
        }
        
        String action = request.getParameter("action");
        if ("delete".equals(action)) {
        	 try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
                 String sql = "DELETE FROM employees WHERE employee_id = ?";
                 PreparedStatement ps = conn.prepareStatement(sql);
                 ps.setInt(1, empId);  // ← 削除対象のIDを指定
                 int rowsAffected = ps.executeUpdate(); // ← 実行する

                 if (rowsAffected > 0) {
                     request.setAttribute("deleted", "success");
                 } else {
                     request.setAttribute("deleted", "notFound");
                 }
                 
        	 } catch (Exception e) {
                 throw new ServletException("更新処理中にエラーが発生しました: " + e.getMessage(), e);
             }
        	
            request.getRequestDispatcher("/WEB-INF/view/staffDeleteResult.jsp").forward(request, response);
        } else if ("search".equals(action)) {
            request.getRequestDispatcher("/WEB-INF/view/staffDeleteSearch.jsp").forward(request, response);
            
        }
        
        
        //request.getRequestDispatcher("/WEB-INF/view/staffDelete.jsp").forward(request, response);
    }

    // POSTでもGETと同じ処理を使う
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

}

