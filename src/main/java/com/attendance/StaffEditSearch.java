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

@WebServlet("/staffEditSearch")
public class StaffEditSearch extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private String dbURL = "jdbc:mysql://192.168.0.154:3306/demoDB?serverTimezone=UTC";
    private String dbUser = "remote_user";
    private String dbPass = "password";

    // GETメソッドに対応（主処理ここ）
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String empIdStr = request.getParameter("employeeId");
        Integer empId = null;
        String name = null;

        if (empIdStr == null || empIdStr.trim().isEmpty()) {
            request.setAttribute("searched", "emptyId");
            request.getRequestDispatcher("/WEB-INF/view/staffEditSearch.jsp").forward(request, response);
            return;
        }

        try {
            empId = Integer.parseInt(empIdStr);

            Class.forName("com.mysql.cj.jdbc.Driver");

            try (Connection conn = DriverManager.getConnection(dbURL, dbUser, dbPass);
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
        if ("edit".equals(action)) {
            request.getRequestDispatcher("/WEB-INF/view/staffEdit.jsp").forward(request, response);
        } else if ("search".equals(action)) {
            request.getRequestDispatcher("/WEB-INF/view/staffEditSearch.jsp").forward(request, response);
        }
        
        
        //request.getRequestDispatcher("/WEB-INF/view/staffEdit.jsp").forward(request, response);
    }

    // POSTでもGETと同じ処理を使う
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

}

