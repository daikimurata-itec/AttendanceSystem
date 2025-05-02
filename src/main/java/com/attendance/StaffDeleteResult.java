package com.attendance;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/staffDeleteResult")
public class StaffDeleteResult extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // GETで直接アクセスされた場合はフォーム画面にリダイレクト
        response.sendRedirect("staffDelete");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // POST経由でもう一度結果画面を表示させたい場合用（基本は使わないかも）
        request.getRequestDispatcher("/WEB-INF/view/staffDeleteResult.jsp").forward(request, response);
    }
}