package com.attendance;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.attendance.dao.AttendanceDAO;

@WebServlet("/clockIn")
public class ClockIn extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private AttendanceDAO attendanceDAO = new AttendanceDAO();
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm:ss");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        LocalDateTime now = LocalDateTime.now();
        request.setAttribute("currentTime", now.format(TIME_FMT));

        HttpSession session = request.getSession(false);
        if (session != null) {
            request.setAttribute("employeeId", session.getAttribute("employeeId"));
            request.setAttribute("employeeName", session.getAttribute("employeeName"));
        }

        request.getRequestDispatcher("/WEB-INF/view/clockIn.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        LocalDateTime now = LocalDateTime.now();
        String timeStr = now.format(TIME_FMT);

        HttpSession session = request.getSession(false);
        int employeeId = 0;
        if (session != null && session.getAttribute("employeeId") != null) {
            employeeId = (Integer) session.getAttribute("employeeId");
        }

        try {
            attendanceDAO.insertPunch(employeeId, action, now);
        } catch (Exception e) {
            throw new ServletException("打刻の保存に失敗しました", e);
        }

        request.setAttribute("currentTime", timeStr);
        request.setAttribute("actionTime", timeStr + " に「" + action + "」を記録しました");
        request.setAttribute("employeeId", employeeId);
        request.setAttribute("employeeName", session.getAttribute("employeeName"));

        request.getRequestDispatcher("/WEB-INF/view/clockIn.jsp").forward(request, response);
    }
}
