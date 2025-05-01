package com.attendance;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/staffRegist")
public class StaffRegist extends HttpServlet {

    // GETでアクセスされたとき → 登録フォームへ遷移
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/view/staffRegist.jsp").forward(request, response);
    }

    // POSTで送信されたとき → DB登録処理
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        

        if (name == null || name.isEmpty() || email == null || email.isEmpty() || password == null || password.isEmpty()) {
            request.setAttribute("error", "名前・メールアドレス・パスワードは必須です。");
            request.setAttribute("name", name);       // 入力値の保持（任意）
            request.setAttribute("email", email);     // 入力値の保持（任意）
            request.getRequestDispatcher("/WEB-INF/view/staffRegist.jsp").forward(request, response);  // ← 入力フォームに戻る
            return;
        }

        String dbURL = "jdbc:mysql://192.168.0.154:3306/demoDB";
        String dbUser = "remote_user";
        String dbPassword = "password";

        int generatedId = 0;
        try {
            // MySQL JDBCドライバを手動でロード
            Class.forName("com.mysql.cj.jdbc.Driver");

            try (Connection conn = DriverManager.getConnection(dbURL, dbUser, dbPassword)) {
                String sql = "INSERT INTO employees (name, email, password) VALUES (?, ?, ?)";
                PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, name);
                ps.setString(2, email);
                ps.setString(3, password);
                int result = ps.executeUpdate();

                ResultSet rs = ps.getGeneratedKeys();

                if (rs.next()) {
                    generatedId = rs.getInt(1);
                    System.out.println("Generated ID: " + generatedId);
                } else {
                    System.out.println("自動生成されたIDは取得できませんでした");
                }

                request.setAttribute("id", generatedId);
                request.setAttribute("name", name);
                request.getRequestDispatcher("/WEB-INF/view/staffRegistResult.jsp").forward(request, response);

            } catch (Exception e) {
                throw new ServletException("DB接続または処理中にエラーが発生しました: " + e.getMessage(), e);
            }

        } catch (ClassNotFoundException e) {
            throw new ServletException("MySQL JDBCドライバが見つかりません: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new ServletException("予期しないエラーが発生しました: " + e.getMessage(), e);
        }
    }
}