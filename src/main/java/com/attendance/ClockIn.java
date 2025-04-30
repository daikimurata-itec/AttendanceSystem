package com.attendance;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/clockIn")
public class ClockIn extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 現在時刻を取得
        LocalDateTime now = LocalDateTime.now();
        String currentTime = now.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        request.setAttribute("currentTime", currentTime);

        request.getRequestDispatcher("/WEB-INF/view/clockIn.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        LocalDateTime now = LocalDateTime.now();
        String time = now.format(DateTimeFormatter.ofPattern("HH:mm:ss"));

        // 打刻結果をリクエスト属性に設定
        String result = time + " " + action;
        request.setAttribute("currentTime", time);
        request.setAttribute("actionTime", result);

        request.getRequestDispatcher("/WEB-INF/view/clockIn.jsp").forward(request, response);
    }
}