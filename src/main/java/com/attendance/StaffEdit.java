package com.attendance;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/staffEdit")
public class StaffEdit extends HttpServlet {

    private static final String DB_URL = "jdbc:mysql://192.168.0.154:3306/demoDB?serverTimezone=UTC";
    private static final String DB_USER = "remote_user";
    private static final String DB_PASS = "password";

    // 編集画面に遷移（事前にデータ取得）
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idStr = request.getParameter("id");
        if (idStr == null || idStr.isEmpty()) {
            response.sendRedirect("staffEditSearch"); // IDがないなら検索画面に戻す
            return;
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
                PreparedStatement stmt = conn.prepareStatement("SELECT name, email FROM employees WHERE employee_id = ?");
                stmt.setInt(1, Integer.parseInt(idStr));
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    request.setAttribute("id", idStr);
                    request.setAttribute("name", rs.getString("name"));
                    request.setAttribute("email", rs.getString("email"));
                } else {
                    request.setAttribute("error", "スタッフが見つかりませんでした。");
                }

            }

        } catch (Exception e) {
            throw new ServletException("DBエラー: " + e.getMessage(), e);
        }

        request.getRequestDispatcher("/WEB-INF/view/staffEdit.jsp").forward(request, response);
    }

    // 編集内容の更新処理
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idStr = request.getParameter("id");
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // バリデーション
        if (idStr == null || idStr.isEmpty() ||
            name == null || name.isEmpty() ||
            email == null || email.isEmpty() ||
            password == null || password.isEmpty()) {

            request.setAttribute("error", "全ての項目を入力してください。");
            request.setAttribute("id", idStr);
            request.setAttribute("name", name);
            request.setAttribute("email", email);
            request.getRequestDispatcher("/WEB-INF/view/staffEdit.jsp").forward(request, response);
            return;
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
                String sql = "UPDATE employees SET name = ?, email = ?, password = ? WHERE employee_id = ?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, name);
                ps.setString(2, email);
                ps.setString(3, password);
                ps.setInt(4, Integer.parseInt(idStr));

                int updated = ps.executeUpdate();
                if (updated > 0) {
                    request.setAttribute("id", idStr);
                    request.setAttribute("name", name);
                    request.getRequestDispatcher("/WEB-INF/view/staffEditResult.jsp").forward(request, response);
                } else {
                    request.setAttribute("error", "更新に失敗しました。");
                    request.getRequestDispatcher("/WEB-INF/view/staffEdit.jsp").forward(request, response);
                }
            }

        } catch (Exception e) {
            throw new ServletException("更新処理中にエラーが発生しました: " + e.getMessage(), e);
        }
    }

}

